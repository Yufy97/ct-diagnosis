#coding=utf-8
import sys
import numpy as np
from PIL import Image
import matplotlib.pyplot as plt
import paddle
from paddle import nn
from paddle.nn import functional as F
import requests
from io import BytesIO
import warnings
import os
warnings.filterwarnings("ignore", category=DeprecationWarning)

"""
设置超参数
"""
BATCH_SIZE = 16      # 每批次的样本数
EPOCHS = 8           # 训练轮数
LOG_GAP = 10         # 输出训练信息的间隔

CLASS_DIM = 4        # 图像种类
LAB_DICT = {         # 记录标签和数字的关系
    "0": "正常肺部",
    "1": "病毒性肺炎",
    "2": "新冠肺炎",
    "3": "肺结核",
}

INIT_LR = 3e-4       # 初始学习率
LR_DECAY = 0.6       # 学习率衰减率

DST_PATH = "./data"                 # 解压路径
DATA_PATH = {                       # 实验数据集路径
    "0": DST_PATH + "/input_data/NORMAL",
    "1": DST_PATH + "/input_data/Viral_Pneumonia",
    "2": DST_PATH + "/input_data/COVID",
    "3": DST_PATH + "/input_data/Pul_Tuberculosis",
}
MODEL_PATH = os.path.dirname(os.path.abspath(__file__)) + "/ResNet.pdparams"      # 模型参数保存路径

def data_mapper(img_path, show=False):
    ''' 图像处理函数 '''
    img = Image.open(img_path).convert("RGB")  # 以RGB模式打开图片
    # 将其缩放为224*224的高质量图像：
    img = img.resize((224, 224), Image.ANTIALIAS)
    # 这个是在notebook中展示图片，在pycharm中不能用
    # if show:  # 展示图像
    #     display(img)
    if show:   # pycharm中展示图片
        plt.imshow(img)
        plt.show()
    # 把图像变成一个numpy数组以匹配数据馈送格式：
    img = np.array(img).astype("float32")
    # 将图像矩阵由“rgb,rgb,rbg...”转置为“rr...,gg...,bb...”：
    img = img.transpose((2, 0, 1))
    # 将图像数据归一化，并转换成Tensor格式：
    img = paddle.to_tensor(img / 255.0)
    return img

"""
网络配置
"""
class ConvBN2d(nn.Layer):
    ''' Conv2d with BatchNorm2d and ReLU '''

    def __init__(self, in_channels: int, out_channels: int,
                 kernel_size: int, stride=1, padding=0, act=None):
        '''
        * `in_channels`: 输入通道数
        * `out_channels`: 输出通道数
        * `kernel_size`: 卷积核大小
        * `stride`: 卷积运算的步长
        * `padding`: 卷积填充的大小
        * `act`: 激活函数（None / relu）
        '''
        super(ConvBN2d, self).__init__()
        self.act = act
        self.net = nn.Sequential(
            nn.Conv2D(in_channels, out_channels, kernel_size, stride, padding),
            # 批标准化（batch normalization）层。它被用于卷积神经网络（CNN）中，用于规范化输入的特征图。
            nn.BatchNorm2D(out_channels)
        )

    def forward(self, x):
        if self.act == "relu":
            return F.relu(self.net(x))
        else:
            return self.net(x)


class BasicBlock(nn.Layer):
    ''' A Residual Block for ResNet-18/34 '''
    expansion = 1       # 最后一层输出的通道扩展倍数

    def __init__(self, in_size, out_size, stride=1):
        '''
        * `in_size`: 第一层卷积层的输入通道数
        * `out_size`: 第一层卷积层的输出通道数
        * `stride`: 第一层卷积层的运算步长
        '''
        super(BasicBlock, self).__init__()
        end_size = self.expansion * out_size    # 最后一层卷积层的输出通道数

        self.layers = nn.Sequential(
            ConvBN2d(in_size, out_size, 3, stride, 1, "relu"),
            ConvBN2d(out_size, end_size, 3, 1, 1, None),
        )
        if in_size != end_size:     # 进行拼接之前需要统一通道数和尺寸
            self.shortcut = ConvBN2d(in_size, end_size, 1, stride, act=None)
        else:
            self.shortcut = None

    def forward(self, x):
        """
        拼接操作是为了实现残差连接（residual connection）。
        残差连接是一种在深层网络中常用的技术，可以帮助缓解梯度消失问题，并提高模型的收敛性和准确性。
        """
        fx = self.layers(x)
        if self.shortcut is not None:
            x = self.shortcut(x)
        y = F.relu(fx + x)
        return y


class Bottleneck(nn.Layer):
    ''' A Residual Block for ResNet-50/101/152 '''
    expansion = 4       # 最后一层输出的通道扩展倍数

    def __init__(self, in_size, out_size, stride=1):
        '''
        * `in_size`: 第一层卷积层的输入通道数
        * `out_size`: 第一层卷积层的输出通道数
        * `stride`: 第一层卷积层的运算步长
        '''
        super(Bottleneck, self).__init__()
        end_size = self.expansion * out_size    # 最后一层卷积层的输出通道数

        self.layers = nn.Sequential(
            ConvBN2d(in_size, out_size, 1, act="relu"),
            ConvBN2d(out_size, out_size, 3, stride, 1, "relu"),
            ConvBN2d(out_size, end_size, 1, act=None),
        )
        if in_size != end_size:     # 进行拼接之前需要统一通道数和尺寸
            self.shortcut = ConvBN2d(in_size, end_size, 1, stride, act=None)
        else:
            self.shortcut = None

    def forward(self, x):
        """
        拼接操作是为了实现残差连接（residual connection）。
        残差连接是一种在深层网络中常用的技术，可以帮助缓解梯度消失问题，并提高模型的收敛性和准确性。
        """
        fx = self.layers(x)
        if self.shortcut is not None:
            x = self.shortcut(x)
        y = F.relu(fx + x)
        return y


class ResNet(nn.Layer):
    def __init__(self, in_channels=3, n_classes=2, mtype=50):
        '''
        * `in_channels`: 输入的通道数
        * `n_classes`: 输出分类数量
        * `mtype`: ResNet类型（18/34/50/101/152）
        '''
        super(ResNet, self).__init__()
        if mtype == 18:         # ResNet-18
            self.Block, n_blocks = BasicBlock, [2, 2, 2, 2]
        elif mtype == 34:       # ResNet-34
            self.Block, n_blocks = BasicBlock, [3, 4, 6, 3]
        elif mtype == 50:       # ResNet-50
            self.Block, n_blocks = Bottleneck, [3, 4, 6, 3]
        elif mtype == 101:      # ResNet-101
            self.Block, n_blocks = Bottleneck, [3, 4, 23, 3]
        elif mtype == 152:      # ResNet-152
            self.Block, n_blocks = Bottleneck, [3, 8, 36, 3]
        else:
            raise NotImplementedError("`mtype` must in [18, 34, 50, 101, 152]")
        self.e = self.Block.expansion       # 残差结构输出通道数的扩展倍数

        self.conv1 = ConvBN2d(in_channels, out_channels=64, kernel_size=7, stride=2, padding=3, act="relu")
        self.pool1 = nn.MaxPool2D(3, 2, 1)
        self.conv2 = self._res_blocks(n_blocks[0], 64, 64, 1)   # 本层不改变尺寸
        self.conv3 = self._res_blocks(n_blocks[1], 64 * self.e, 128, 2)
        self.conv4 = self._res_blocks(n_blocks[2], 128 * self.e, 256, 2)
        self.conv5 = self._res_blocks(n_blocks[3], 256 * self.e, 512, 2)
        self.pool2 = nn.AdaptiveAvgPool2D((1, 1))
        self.linear = nn.Sequential(nn.Flatten(1, -1),
                                    nn.Linear(512 * self.e, n_classes))

    def forward(self, x):
        x = self.conv1(x)   # 64*112*112
        x = self.pool1(x)   # 64*56*56
        x = self.conv2(x)   # 64*56*56  or 256*56*56
        x = self.conv3(x)   # 128*28*28 or 512*28*28
        x = self.conv4(x)   # 256*14*14 or 1024*14*14
        x = self.conv5(x)   # 512*7*7   or 2048*7*7
        x = self.pool2(x)   # 512*1*1   or 2048*1*1
        y = self.linear(x)  # n_classes
        return y

    def _res_blocks(self, n_block, in_size, out_size, stride):
        '''
        * `n_block`: 残差块的数量
        * `in_size`: 第一层卷积层的输入通道数
        * `out_size`: 第一层卷积层的输出通道数
        * `stride`: 第一个残差块卷积运算的步长
        '''
        blocks = [self.Block(in_size, out_size, stride), ]
        in_size = out_size * self.e     # 后续残差块的输入通道数
        for _ in range(1, n_block):
            blocks.append(self.Block(in_size, out_size, stride=1))
        return nn.Sequential(*blocks)

def data_mapper(img_path, augment=None, show=False):
    ''' 图像处理函数 '''
    img = Image.open(img_path).convert("RGB")  # 以RGB模式打开图片
    # 将其缩放为224*224的高质量图像：
    img = img.resize((224, 224), Image.ANTIALIAS)
    if show:   # pycharm中展示图片
        plt.imshow(img)
        plt.show()
    if augment is not None:  # 数据增强
        img = augment(img)
    # 把图像变成一个numpy数组以匹配数据馈送格式：
    img = np.array(img).astype("float32")
    # 将图像矩阵由“rgb,rgb,rbg...”转置为“rr...,gg...,bb...”：
    img = img.transpose((2, 0, 1))
    # 将图像数据归一化，并转换成Tensor格式：
    img = paddle.to_tensor(img / 255.0)
    return img

# 预测
def predict_from_url(model, url):
    # 从URL下载图片
    response = requests.get(url)
    img_data = BytesIO(response.content)

    img = data_mapper(img_path=img_data)
    result = model(img[np.newaxis, :, :, :])  # 开始模型预测
    key = str(np.argmax(result))
    infer_lab = LAB_DICT[key]  # 获取预测结果
    return key,infer_lab

if __name__ == '__main__':
    # 实例化模型
    model = ResNet(in_channels=3, n_classes=CLASS_DIM, mtype=50)  # ResNet-50

    """
    模型预测
    """
    # print("start testing....")
    model.eval()  # 开启评估模式
    model.set_state_dict(
        paddle.load(MODEL_PATH)
    )  # 载入预训练模型参数

    # 图片URL
    img_url = sys.argv[1]
    # img_url = 'http://qiniu.gwithl.cn/ct/NORMAL%20%2812%29.png'

    # 预测结果
    xvhao, infer_lab = predict_from_url(model, img_url)
    # print("预测结果：%s" % (infer_lab))
    print(xvhao+";"+infer_lab)