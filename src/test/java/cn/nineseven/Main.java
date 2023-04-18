package cn.nineseven;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc. nextInt();
        int[] a = new int[n];
        List<Boolean> fl = new ArrayList<>();
        a[0] = 2;a[1] = 4;a[2] = 1;
        for(int i = 0; i < n; i++){
            a[i] = sc.nextInt();
            for(int j = 0; j < a[i]; j++){
                fl.add(true);
            }
            fl.add(false);
        }
        int mod = fl.size();
        String s;
        int cnt = 0;
        while(!(s = sc.next()).equals("End")){
            if(s.equals("ChuiZi")){
                if(fl.get(cnt % mod)){
                    writer.println("Bu");
                }else{
                    writer.println("JianDao");
                }
            } else if(s.equals("JianDao")){
                if(fl.get(cnt % mod)){
                    writer.println("ChuiZi");
                }else{
                    writer.println("Bu");
                }
            } else if(s.equals("Bu")){
                if(fl.get(cnt % mod)){
                    writer.println("JianDao");
                }else{
                    writer.println("ChuiZi");
                }
            }
            cnt++;
        }
        writer.flush();
    }
}
