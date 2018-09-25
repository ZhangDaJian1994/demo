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
        String expression = "20^65";
//        String expression = "((1/3-0.3333333333333333235)+(1/3-0.333333333333333759)*0.008)*10^20";
//        String expression = "1-2";
        Tree root = Calculator.conversion(expression).pop();
        long a= System.currentTimeMillis();//获取当前系统时间(毫秒)
        System.out.print(ArithHelper.Main(root, epslion).setScale(PRE, 6).stripTrailingZeros().toString());
        System.out.print("程序执行时间为：");
        System.out.println(System.currentTimeMillis()-a+"毫秒");


    }

}