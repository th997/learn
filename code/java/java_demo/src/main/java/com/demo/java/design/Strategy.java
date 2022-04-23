package com.demo.java.design;

public class Strategy {

    public static void main(String[] args) {
        Strategy strategy = new Strategy();
        strategy.printService = new CnPrintServiceImpl();
        strategy.smartDoPrint();
        strategy.printService = new InPrintServiceImpl();
        strategy.smartDoPrint();
    }

    public void smartDoPrint() {
        printService.doPrint();
    }

    private PrintService printService;

    //

    public interface PrintService {
        void doPrint();
    }

    public static class CnPrintServiceImpl implements PrintService {
        @Override
        public void doPrint() {
            System.out.println("cn print");
        }
    }

    public static class InPrintServiceImpl implements PrintService {
        @Override
        public void doPrint() {
            System.out.println("in print");
        }
    }
}
