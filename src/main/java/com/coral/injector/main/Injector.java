package com.coral.injector.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class Injector {

    static {
        System.loadLibrary("attach");
    }

    public static Scanner in;
    public static boolean mcRunning = false;

    public static int pid = 0;

    public static void main(String[] args)
            throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        in = new Scanner(System.in);

        String javawCommand = "tasklist /v /FI \"IMAGENAME eq javaw.exe\"";
        InputStream is = Runtime.getRuntime().exec(javawCommand).getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String s = "";

        while ((s = br.readLine()) != null) {
            if (s.contains("Minecraft 1.")) {
                mcRunning = true;
                break;
            }
        }
        if (!mcRunning) {
            System.out.println("Please start Minecraft and relaunch this program!");
            Runtime.getRuntime().exit(0);
        }

        System.out.print("Please enter the PID of minecraft: ");
        pid = Integer.parseInt(in.nextLine());
        System.out.println();

        if (pid == 0) {
            System.out.println("Please relaunch the program and enter a valid pid!");
            Runtime.getRuntime().exit(0);
        }

        VirtualMachine vm = VirtualMachine.attach(String.valueOf(pid));
        System.out.println("Enter the FULL path of this jar");
        String path = in.nextLine();
        vm.loadAgent(path, null);
        vm.detach();

    }

}