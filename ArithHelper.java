import java.math.BigDecimal;




class Tree {
    String value;
    Tree left;
    Tree right;
}
public class ArithHelper {


    public static BigDecimal Two = new BigDecimal(2L);
    public static BigDecimal ZeroDone = new BigDecimal("0.1");
    public static BigDecimal Four = new BigDecimal(4);
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 16;
    private static double epslion = Math.pow(10, -10);
    // 这个类不能实例化
    private ArithHelper() {
    }


    /**
     * 算法1
     * @param a
     * @param epslion
     * @return
     */
    public static BigDecimal Main(Tree a, BigDecimal epslion) {
        if (!isOp(a.value)) {   //a是常数
            return new BigDecimal(a.value); ////直接返回a 或在精度" 的条件下将其截断.
        }else if (is4Op(a.value) && !isOp(a.left.value) && !isOp(a.right.value)) {   //a 是两个常数的四则运算
            BigDecimal left = new BigDecimal(a.left.value);
            BigDecimal right = new BigDecimal(a.right.value);
            //两个常数的四则运算的结果; //由假设知, 可以获得两个常数的四则运算任意精度的值
            //TODO 补充精度相关内容
            switch (a.value) {
                case '+':
                    return left.add(right);
                    break;
                case '-':
                    return left.subtract(right);
                    break;
                case '*':
                    return left.multiply(right);
                    break;
                case '/':
                    return left.divide(right);
                    break;
                default:
            }
        }else if (isOp(a.value) && a.value.equals("*")) {   //若a 是两个表达式的积, 则调用算法2.Mul(a1; a2; ")
            return mul(a.left, a.right, epslion);
        }else if (isOp(a.value) && a.value.equals("/")) {
            return div(a.left, a.right, epslion);
            }
        }


    /**
     * 算法3
     * @param left
     * @param right
     * @param epslion2
     * @return
     */
    private static BigDecimal div(Tree left, Tree right, BigDecimal epslion) {
        BigDecimal a1_ = Main(left, new BigDecimal("0.1"));
        BigDecimal a1_abs = a1_.abs();
        BigDecimal a1_abs_up = a1_abs.add(ZeroDone);
        BigDecimal epslion2 = ZeroDone;
        BigDecimal a2_ = Main(right, epslion2);
        int r = a2_.abs().compareTo(Two.multiply(epslion2));
        while (r == 0 || r == -1) {  //|˜ a 2 | <= 2 × ε 2
            epslion2 = ZeroDone.multiply(epslion2);
            a2_ = Main(right, epslion2);
        }
        BigDecimal a2_abs = a2_.abs();
        BigDecimal a2_abs_down = a2_abs.subtract(epslion2);
        int r1 = Four.multiply(a1_abs_up).multiply(epslion2).abs().compareTo(a2_.multiply(a2_abs_down).multiply(epslion).abs());
        while (r1 == 0 || r1 == 1) {
            epslion2 = ZeroDone.multiply(epslion2);
            a2_ = Main(right, epslion2);
        }
        a1_ = Main(left, cons(a2_abs.divide(Four).multiply(epslion)));
        Tree t = new Tree();
        t.value = a1_.divide(a2_).toString();
        t.left = null;
        t.right = null;
        return Main(t, epslion.divide(Two));
    }

    /**
     * 算法2 乘法
     * @param c
     * @param epslion
     * @return
     */
    public static BigDecimal mul(Tree a1, Tree a2, BigDecimal epslion) {
        BigDecimal a2_ = Main(a2, new BigDecimal("0.1"));
        BigDecimal a2_abs = a2_.abs();
        BigDecimal epslion_1 = cons(epslion.divide(Two.multiply(a2_abs.add(new BigDecimal("0.1"))))); //BigDecimal epslion_1 = cons(epslion/(2*(Math.abs(a2_)+0.1)));
        BigDecimal a1_ = Main(a1, epslion_1);
        BigDecimal a1_abs = a1_.abs();
        BigDecimal epslion_2 = cons(epslion.divide(Two.multiply(a1_abs)));//BigDecimal epslion_2 = cons(epslion/(2*Math.abs(a1_)));
        a2_ = Main(a2, epslion_2);
        return a1_.multiply(a2_);
    }
    /**
     * 算法8
     * @param v1
     * @return
     */

     public static BigDecimal lnl(Tree t, BigDecimal epslion) {
        BigDecimal c = new BigDecimal(t.value);
         if(c.equals("1")) {
             return BigDecimal.ZERO;
         }
         else {
             BigDecimal n = BigDecimal.ONE;
             BigDecimal c_1 = c.subtract(BigDecimal.ONE);
             BigDecimal c_1_abs = c_1.abs();
             BigDecimal four_n_c = Four.multiply(c).multiply(n);
             BigDecimal r = Two.multiply(c_1_abs.pow(2*n.intValue()+1)).
                compareTo(four_n_c.multiply(c.add(BigDecimal.ONE).pow(2*n.intValue()-1)).multiply(epslion)); //
             while (r == 0 || r == 1) {
                 n = n.add(BigDecimal.ONE);
             }
            //求前n项表达式之和
            BigDecimal sum = 0;
            BigDecimal cc = c.subtract(BigDecimal.ONE).divide(c.add(BigDecimal.ONE));// c-1/c+1
             for (int i = 0; i < n.intValue(); i++) {
                 BigDecimal k = new BigDecimal(i);
                BigDecimal item = Two.divide(Two.multiply(k).add(BigDecimal.ONE));
                sum = sum.add(item.multiply(cc.pow(Two.multiply(k).add(BigDecimal.ONE).intValue())));
             }
             Tree t1 = new Tree();
             t1.value = sum.toString();
             t1.left = null;
             t1.right = null;
             return Main(t1, epslion.divide(Two));
         }

     }
    /**
     * 算法9
     * @param v
     * @param scale
     * @return
     */
    public static BigDecimal ln(Tree a, BigDecimal epslion) {
        if (!isOp(a.value)) {
            return lnl(a, epslion);
        }
        else {
            BigDecimal epslion_ = ZeroDone;
            BigDecimal a_ = Main(a, epslion_);
            BigDecimal a_abs = a_.abs();
            int r1 = a_abs.compareTo(Two.multiply(epslion_));
            int r2 = Two.multiply(epslion_).compareTo(a_abs.subtract(epslion_).multiply(epslion));
            while ((r1 == 0 || r1 == -1) && r2 == 1 ){
                epslion_ = ZeroDone.multiply(epslion_);
                a_ = Main(a, epslion_);
            }
            Tree t = new Tree();
            t.value = a_.toString();
            t.left = null;
            t.right = null;
            return lnl(t, epslion.divide(Two));
        }
    }
    /**
     * 算法10 e^c c为常数
     */
    public static BigDecimal exp1(Tree a, BigDecimal epslion) {
        BigDecimal c = new BigDecimal(a.value);
        int cc = c.abs().intValue();
        int n = Math.max(cc, 1);
        BigDecimal nn = new BigDecimal(n);

        int r = Two.multiply(c.pow(n)).compareTo(nn.subtract(c).multiply(epslion)
            .multiply(factorial(nn.subtract(BigDecimal.ONE))));
        
        while ( r == 0 || r == 1) {
             n++;
        }

        BigDecimal sum = 0;
        /// 计算式(19) 中前 n 项的和 .
        for (int i = 0; i < n; i++) {
            BigDecimal k = new BigDecimal(i);
            BigDecimal fm = factorial(k);
            sum = sum.add(BigDecimal.ONE.divide(fm).multiply(c.pow(i)));
        }
        Tree t = new Tree();
        t.value = sum.toString();
        t.left = null;
        t.right = null;
        return Main(t, epslion.divide(Two));
    }
    /**
     * 算法11 e^a
     */
    public static BigDecimal exp(Tree a, BigDecimal epslion) {
        BigDecimal epslion_ = ZeroDone;
        BigDecimal a_ = Main(a, epslion_);
        BigDecimal epslion__ = ZeroDone.add(ZeroDone);
        Tree t = new Tree();
        t.value = a_.add(epslion_).toString();
        t.left = null;
        t.right = null;
        BigDecimal y1 = exp1(t, epslion__);
        a1_ = Main(a, cons(epslion.divide(Two.multiply(y1.add(epslion__)))));
        t = new Tree();
        t.value = a1_.toString();
        t.left = null;
        t.right = null;
        return exp1(t, epslion.divide(Two));
    }

    /**
     * 算法12 计算a1^a2
     */
    // public static double exp(double a1, double a2, double epslion) {
    //     if (a1 == 0)
    //         return 0;
    //     else if (a1 > 0 )
    //         return Main(Math.pow(Math.E, a2*Math.log(a1)), epslion);
    //     else if (a2 % 2 == 0 )    //TODO 先这样写，后面再修改
    //         return Main(Math.pow(Math.E, a2*Math.log(-a1)), epslion);
    //     else    
    //         return 0-Main(Math.pow(Math.E, a2*Math.log(-a1)), epslion);

    // }

    /**
     * 求阶乘
     */
    public static BigDecimal factorial(BigDecimal n) {
        int r = n.compareTo(BigDecimal.ONE);
        if (r == 0 || r == -1)
            return BigDecimal.ONE;
        else
            return n.multiply(factorial(n.subtract(BigDecimal.ONE)));
    }
    /**
     * 生成精度
     * @param a
     * @return
     */
    public static BigDecimal cons(BigDecimal a) {
        int n = -1;
        while (BigDecimal.TEN.pow(n).compareTo(a) > 0) {
            n--;
        }
        return BigDecimal.TEN.pow(n);
    }
   
    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        java.math.BigDecimal b = new java.math.BigDecimal(Double.toString(v));
        java.math.BigDecimal one = new java.math.BigDecimal("1");
        return b.divide(one, scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double round(String v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        java.math.BigDecimal b = new java.math.BigDecimal(v);
        java.math.BigDecimal one = new java.math.BigDecimal("1");
        return b.divide(one, scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
    }



    private static boolean isOp(String str) {
        if ("+-*/^ln".contains(str)){
            return true;
        }
        return false;
    }

    private static boolean is4Op(String str) {
        if ("+-*/".contains(str)){
            return true;
        }
        return false;
    }
}