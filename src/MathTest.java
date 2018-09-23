import java.math.BigDecimal;
import java.util.Scanner;


public class MathTest {
    public static  int PRE = 1;
    public static void main(String[] args) {
        // String expression = "(0*1--3)-5/-4-(3*(-2.13))";
        Scanner sc = new Scanner(System.in);
        PRE = sc.nextInt();
        BigDecimal epslion = new BigDecimal(BigDecimal.ONE.divide(BigDecimal.TEN.pow(PRE)).toString());
//        BigDecimal epslion = new BigDecimal("0.00000001");
        String expression = "20^65-e^(65*ln(20))";
        //((1/3−0.3333333333333333235)+(1/3−0.333333333333333759)*0.008)*10^20
        Tree root = Calculator.conversion(expression).pop();
        System.out.print(ArithHelper.Main(root, epslion).toString());
//        System.out.print(ArithHelper.cons(epslion.divide(ArithHelper.Two, ArithHelper.def_scale, 6)));

         // String doubleR
        //
        // eg = "[-+]?([0-9]+(\\.[0-9]+)?|π|e)";
        // if (expression.matches("^ln"+doubleReg+"$"))
        //     System.out.print(Math.log(Double.valueOf(expression.substring(2))));
        // else{
        //     System.out.print("fhdsjfh");
        // }

        // BigDecimal bd1 = new BigDecimal(20);
        // BigDecimal bd2 = new BigDecimal(65);
        // BigDecimal bd3 =  bd1.pow(bd2.intValue()); //20^65
        // BigDecimal b1 = new BigDecimal(Math.log(20));
        // BigDecimal bd4 = bd2.multiply(b1); //65*ln20
        // BigDecimal e = new BigDecimal(Math.E);
        // Math.pow(e, bd4.doubleValue());


        // BigDecimal bd = new BigDecimal(65 * Math.log(20));
        // bd.
        // BigDecimal bd2 = new BigDecimal(Math.pow(Math.E, bd));

        // double result = Calculator.conversion(expression);
        // // System.out.println(expression + " = " + result);
        // System.out.println();



    }

}