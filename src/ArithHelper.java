import java.math.BigDecimal;

public class ArithHelper {


    public static BigDecimal Two = new BigDecimal(2L);
    public static BigDecimal ZeroDone = new BigDecimal("0.1");
    public static BigDecimal Four = new BigDecimal(4);
    // 默认除法运算精度
    public static  int def_scale = 100;
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
            if (a.value.equals("e")){
                return new BigDecimal(Math.E);
            }else if (a.value.equals("pi")){

            }else {
                return new BigDecimal(a.value).setScale(MathTest.PRE, 6); ////直接返回a 或在精度" 的条件下将其截断.
            }

        }else if (is4Op(a.value) && !isOp(a.left.value) && !isOp(a.right.value)) {   //a 是两个常数的四则运算
            BigDecimal left = new BigDecimal(a.left.value);
            BigDecimal right = new BigDecimal(a.right.value);
            //两个常数的四则运算的结果; //由假设知, 可以获得两个常数的四则运算任意精度的值
            //TODO 补充精度相关内容
            switch (a.value) {
                case "+":
                    return left.add(right);

                case "-":
                    return left.subtract(right);

                case "*":
                    return left.multiply(right);

                case "/":
                    return left.divide(right,def_scale,6);

                default:
            }
        }else if ((a.value.equals("+") || a.value.equals("-")) && (isOp(a.left.value) || isOp(a.right.value))){
            BigDecimal left = Main(a.left, cons(epslion.divide(Two, def_scale, 6)));
            BigDecimal right = Main(a.right,  cons(epslion.divide(Two, def_scale, 6)));
            return a.value.equals("+") ? left.add(right) : left.subtract(right);
        }
        else if (isOp(a.value) && a.value.equals("*")) {   //若a 是两个表达式的积, 则调用算法2.Mul(a1; a2; ")
            return mul(a.left, a.right, epslion);
        }else if (isOp(a.value) && a.value.equals("/")) {
            return div(a.left, a.right, epslion);
        }else if (isOp(a.value) && a.value.equals("ln")) {
            return ln(a.right, epslion);
        }else if (isOp(a.value) && a.value.equals("^") && a.left.value.equals("e")) {
            return exp(a.right, epslion);
        }else if (isOp(a.value) && a.value.equals("^")) {
            return exp(a.left, a.right, epslion);
        }
        return new BigDecimal("0");
        }


    /**
     * 算法3
     * @param left
     * @param right
     * @param
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
            r = a2_.abs().compareTo(Two.multiply(epslion2));
        }
        BigDecimal a2_abs = a2_.abs();
        BigDecimal a2_abs_down = a2_abs.subtract(epslion2);
        int r1 = Four.multiply(a1_abs_up).multiply(epslion2).abs().compareTo(a2_.multiply(a2_abs_down).multiply(epslion).abs());
        while (r1 == 0 || r1 == 1) {
            epslion2 = ZeroDone.multiply(epslion2);
            a2_ = Main(right, epslion2);
            r1 = Four.multiply(a1_abs_up).multiply(epslion2).abs().compareTo(a2_.multiply(a2_abs_down).multiply(epslion).abs());
        }
        a1_ = Main(left, cons(a2_abs.divide(Four, def_scale, 6).multiply(epslion)));
        Tree t = new Tree();
        t.value = a1_.divide(a2_, def_scale, 6).toString();
        t.left = null;
        t.right = null;
        return Main(t, epslion.divide(Two, def_scale, 6));
    }

    /**
     * 算法2 乘法
     * @param
     * @param epslion
     * @return
     */
    public static BigDecimal mul(Tree a1, Tree a2, BigDecimal epslion) {
        BigDecimal a2_ = Main(a2, new BigDecimal("0.1"));
        BigDecimal a2_abs = a2_.abs();
        BigDecimal epslion_1 = cons(epslion.divide(Two.multiply(a2_abs.add(new BigDecimal("0.1"))), def_scale, 6)); //BigDecimal epslion_1 = cons(epslion/(2*(Math.abs(a2_)+0.1)));
        BigDecimal a1_ = Main(a1, epslion_1);
        BigDecimal a1_abs = a1_.abs();
        BigDecimal epslion_2 = cons(epslion.divide(Two.multiply(a1_abs), def_scale, 6));//BigDecimal epslion_2 = cons(epslion/(2*Math.abs(a1_)));
        a2_ = Main(a2, epslion_2);
        return a1_.multiply(a2_);
    }
    /**
     * 算法8
     * @param
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
             int r = Two.multiply(c_1_abs.pow(2*n.intValue()+1)).
                compareTo(four_n_c.multiply(c.add(BigDecimal.ONE).pow(2*n.intValue()-1)).multiply(epslion)); //

//             BigDecimal r1 = Two.multiply(c_1.abs().pow(2*n.intValue()+1).compareTo(four_n_c.multiply(c.add(BigDecimal.ONE).pow(2*n.intValue()-1).multiply(epslion))));

             while (r == 0 || r == 1) {
                 n = n.add(BigDecimal.ONE);
                 four_n_c = Four.multiply(c).multiply(n);
                 r = Two.multiply(c_1_abs.pow(2*n.intValue()+1)).
                         compareTo(four_n_c.multiply(c.add(BigDecimal.ONE).pow(2*n.intValue()-1)).multiply(epslion));
             }
            //求前n项表达式之和
            BigDecimal sum = new BigDecimal("0");
            BigDecimal cc = c.subtract(BigDecimal.ONE).divide(c.add(BigDecimal.ONE), def_scale, 6);// c-1/c+1
             for (int i = 0; i < n.intValue(); i++) {
                 BigDecimal k = new BigDecimal(i);
                BigDecimal item = Two.divide(Two.multiply(k).add(BigDecimal.ONE), def_scale, 6);
                sum = sum.add(item.multiply(cc.pow(Two.multiply(k).add(BigDecimal.ONE).intValue())));
             }
             Tree t1 = new Tree();
             t1.value = sum.toString();
             t1.left = null;
             t1.right = null;
             return Main(t1, epslion.divide(Two, def_scale, 6));
         }

     }
    /**
     * 算法9
     * @param
     * @param
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
                r1 = a_abs.compareTo(Two.multiply(epslion_));
                r2 = Two.multiply(epslion_).compareTo(a_abs.subtract(epslion_).multiply(epslion));
            }
            Tree t = new Tree();
            t.value = a_.toString();
            t.left = null;
            t.right = null;
            return lnl(t, epslion.divide(Two, def_scale, 6));
        }
    }
    /**
     * 算法10 e^c c为常数
     */
    public static BigDecimal exp1(Tree a, BigDecimal epslion) {
        BigDecimal c = new BigDecimal(a.value);
        int cc = (int)Math.ceil(c.abs().doubleValue());
        int n = Math.max(cc, 1);
        BigDecimal nn = new BigDecimal(n);

        int r = Two.multiply(c.pow(n)).compareTo(nn.subtract(c).multiply(epslion)
            .multiply(factorial(nn.subtract(BigDecimal.ONE))));
        
        while ( r == 0 || r == 1) {
            n += 1;
            nn = new BigDecimal(n);
            r = Two.multiply(c.pow(n)).compareTo(nn.subtract(c).multiply(epslion)
                    .multiply(factorial(nn.subtract(BigDecimal.ONE))));
        }

        BigDecimal sum = new BigDecimal("0");
        /// 计算式(19) 中前 n 项的和 .
        for (int i = 0; i < n; i++) {
            BigDecimal fm = factorial(new BigDecimal(i));
            sum = sum.add(c.pow(i).divide(fm, def_scale, 6));
//            sum = sum.add(BigDecimal.ONE.divide(fm).multiply(c.pow(i)));
        }
        Tree t = new Tree();
        t.value = sum.toString();
        t.left = null;
        t.right = null;
        return Main(t, epslion.divide(Two, def_scale, 6));
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
        BigDecimal a1_ = Main(a, cons(epslion.divide(Two.multiply(y1.add(epslion__)), 100, 6)));
        t = new Tree();
        t.value = a1_.toString();
        t.left = null;
        t.right = null;
        return exp1(t, epslion.divide(Two, def_scale, 6));
    }

    /**
     * 算法12 计算a1^a2
     */
     public static BigDecimal exp(Tree a1, Tree a2, BigDecimal epslion) {
         BigDecimal bd_a1 = new BigDecimal(a1.value);
         if (bd_a1.equals(BigDecimal.ZERO))
             return BigDecimal.ZERO;
         else if (bd_a1.compareTo(BigDecimal.ZERO) > 0){
             String newExp = "e^("+a2.value+"*ln"+a1.value+")";
             Tree root = Calculator.conversion(newExp).pop();
             return Main(root, epslion);
         }else if (Double.parseDouble(xs2fs(Double.parseDouble(a2.value))) % 2 == 0) {
             String newExp = "e^("+a2.value+"*ln"+BigDecimal.ZERO.
                     subtract(new BigDecimal(a1.value))+")";
             Tree root = Calculator.conversion(newExp).pop();
             return Main(root, epslion);
         }else {
             String newExp = "e^("+a2.value+"*ln"+BigDecimal.ZERO.
                     subtract(new BigDecimal(a1.value))+")";
             Tree root = Calculator.conversion(newExp).pop();
             return BigDecimal.ZERO.subtract(Main(root, epslion));
         }

     }

    /**
     * 求阶乘递归形式
     */
//    public static BigDecimal factorial(BigDecimal n) {
//        int r = n.compareTo(BigDecimal.ONE);
//        if (r == 0 || r == -1)
//            return BigDecimal.ONE;
//        else
//            return n.multiply(factorial(n.subtract(BigDecimal.ONE)));
//    }

    /**
     * 求阶乘非递归形式
     * @param n
     * @return
     */
    public static BigDecimal factorial(BigDecimal n) {
        int r = n.compareTo(BigDecimal.ONE);
        if (r == 0 || r == -1)
            return BigDecimal.ONE;
        else {
            BigDecimal sum = BigDecimal.ONE;
            BigDecimal i = Two;
//            int k = i.compareTo(n);
            for ( ;  i.compareTo(n) == 0 ||  i.compareTo(n) == -1; i = i.add(BigDecimal.ONE)) {
                sum = sum.multiply(i);
            }
            return sum;
        }
    }
    /*
     * 生成精度
     * @param a
     * @return
     */
    public static BigDecimal cons(BigDecimal a) {
        int n = -1;
        int r;
        r = BigDecimal.ONE.divide(BigDecimal.TEN.pow(-n), 100, 6).compareTo(a);
        while (r > 0) {
            n--;
            r = BigDecimal.ONE.divide(BigDecimal.TEN.pow(-n), 100, 6).compareTo(a);
        }
        return BigDecimal.ONE.divide(BigDecimal.TEN.pow(-n), 100, 6);
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

    public static String xs2fs(double fNumber) {

        String sA = String.valueOf(fNumber);

        if (sA.indexOf(".") < 0) {
            // fNumber is an integer

            return sA;
        }

        String sZsbf = sA.substring(0,sA.indexOf(".") );
        String sXsbf = sA.substring(sA.indexOf(".") + 1);

        int nXsws = sXsbf.length() ; //小数位数

        long lFenmu = 1;
        for (int k=0; k< nXsws; k++)
            lFenmu *= 10;

        long lFenzi = Long.parseLong( sZsbf + sXsbf );

        long lXs = (lFenzi < lFenmu) ? lFenzi : lFenmu;

        long j = 1; //最大公约数
        for (j = lXs; j > 1; j --) {
            if (lFenzi % j ==0 && lFenmu % j == 0) {
                break;
            }
        }

        lFenzi = lFenzi / j;
        lFenmu = lFenmu / j;
        return String.valueOf(lFenzi);

//        return String.valueOf(lFenzi) + "/" + String.valueOf(lFenmu) ;

    }



    public static boolean isOp(String str) {
        if ("()+-*/^ln".contains(str)){
            return true;
        }
        return false;
    }

    public static boolean is4Op(String str) {
        if ("+-*/".contains(str)){
            return true;
        }
        return false;
    }

    public static boolean is1Op(String str) {
        if ("lnlogsincox".contains(str)) {
            return true;
        }
        return false;
    }
}