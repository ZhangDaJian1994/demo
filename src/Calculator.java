import java.util.Stack;

/**
 * 算数表达式求值  直接调用Calculator的类方法conversion()  传入算数表达式，将返回一个浮点值结果
 * 如果计算过程错误，将返回一个NaN
 */
public class Calculator {
    private Stack<String> postfixStack = new Stack<String>();// 后缀式栈
    private Stack<String> opStack = new Stack<String>();// 运算符栈
    // private int[] operatPriority = new int[] { 0, 3, 2, 1, -1, 1, 0, 2 };//
    // 运用运算符ASCII码-40做索引的运算符优先级

    // 获取运算符的优先级
    public int getPriority(String str) {
        int a;
        switch (str) {
        case "+":
        case "-":
            a = 1;
            break;
        case "*":
        case "/":
            a = 2;
            break;
        case "(":
            a = 0;
            break;
        case ")":
            a = 5;
            break;
        case "tan":
        case "sin":
        case "cos":
        case "log":
            case "ln":
            a = 3;
            break;
        case "^":
            a = 4;
            break;
        default:
            a = -1;
            break;
        }
        return a;
    }

    public static Stack<Tree> conversion(String expression) {
        // double result = 0;
        Stack<Tree> result_tree = new Stack<>();
        Calculator cal = new Calculator();
        try {
//            expression = transform(expression);
            result_tree = cal.calculate(expression);
        } catch (Exception e) {
            // e.printStackTrace();
            // 运算错误返回NaN
//            return 0.0 / 0.0;
        }
        // return new String().valueOf(result);
        return result_tree;
    }

    /**
     * 将表达式中负数的符号更改
     *
     * @param expression 例如-2+-1*(-3E-2)-(-1) 被转为 ~2+~1*(~3E~2)-(~1)
     * @return
     */
    private static String transform(String expression) {
        char[] arr = expression.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '-') {
                if (i == 0) {
                    arr[i] = '~';
                } else {
                    char c = arr[i - 1];
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
                        arr[i] = '~';
                    }
                }
            }
        }
        if (arr[0] == '~' || arr[1] == '(') {
            arr[0] = '-';
            return "0" + new String(arr);
        } else {
            return new String(arr);
        }
    }

    /**
     * 按照给定的表达式计算
     *
     * @param expression 要计算的表达式例如:5+12*(3+5)/7
     * @return
     */
    public Stack<Tree> calculate(String expression) {
        // Stack<String> resultStack = new Stack<String>();
        prepare(expression); // 将表达式转成后缀表达式
        // System.out.println(postfixStack.toString());
        return create_express_tree(postfixStack);

        
        
        // Collections.reverse(postfixStack);// 将后缀式栈反转
        // String firstValue, secondValue, currentValue;// 参与计算的第一个值，第二个值和算术运算符
        // while (!postfixStack.isEmpty()) {
        //     currentValue = postfixStack.pop();
        //     if (!isOperator(currentValue.charAt(0))) {// 如果不是运算符则存入操作数栈中
        //         currentValue = currentValue.replace("~", "-");
        //         resultStack.push(currentValue);
        //     } else {// 如果是运算符则从操作数栈中取两个值和该数值一起参与运算
        //         secondValue = resultStack.pop();
        //         firstValue = resultStack.pop();

        //         // 将负数标记符改为负号
        //         firstValue = firstValue.replace("~", "-");
        //         secondValue = secondValue.replace("~", "-");

        //         String tempResult = calculate(firstValue, secondValue, currentValue.charAt(0));
        //         resultStack.push(tempResult);
        //     }
        // }
        // return Double.valueOf(resultStack.pop());
    }

    /**
     * 数据准备阶段将表达式转换成为后缀式栈
     * 
     * @param expression
     */
    private void prepare(String expression) {
        opStack.push(String.valueOf(','));// 运算符放入栈底元素逗号，此符号优先级最低
        char[] arr = expression.toCharArray();
        int currentIndex = 0;// 当前字符的位置
        int count = 0;// 上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
        char currentOp;// 当前操作符和栈顶操作符
        String peekOp;
        StringBuffer op_sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            currentOp = arr[i];
            if (ArithHelper.isOp(String.valueOf(currentOp))) {// 如果当前字符是运算符
                if (currentOp == ')') {
                    if (op_sb.length() != 0){
                        opStack.push(op_sb.toString());
                        op_sb.delete(0,op_sb.length());
                    }

                }
                if(ArithHelper.is1Op(String.valueOf(currentOp))) {
                    op_sb.append(currentOp);
                    currentIndex = i + 1;
                    continue;
                }
                if (count > 0) {
                    postfixStack.push(new String(arr, currentIndex, count));// 取两个运算符之间的数字
                }
                peekOp = opStack.peek();
                if (currentOp == ')') {// 遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
                    while (!opStack.peek().equals("(")) {
                        postfixStack.push(String.valueOf(opStack.pop()));
                    }
                    opStack.pop();
                } else {
                    while (currentOp != '(' && !peekOp.equals(",") && compare(currentOp, peekOp)) {
                        postfixStack.push(String.valueOf(opStack.pop()));
                        peekOp = opStack.peek();
                    }
                    opStack.push(String.valueOf(currentOp));
                }
                count = 0;
                currentIndex = i + 1;
            } else {
                count++;
            }
        }
        if (count > 1 || (count == 1 && !ArithHelper.isOp(String.valueOf(arr[currentIndex])))) {// 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
            postfixStack.push(new String(arr, currentIndex, count));
        }

        while (!opStack.peek().equals(",")) {
            postfixStack.push(String.valueOf(opStack.pop()));// 将操作符栈中的剩余的元素添加到后缀式栈中
        }
    }

    /**
     * 判断是否为算术符号
     *
     * @param c
     * @return
     */
//    private boolean isOperator(char c) {
//        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '^';
//    }

    /**
     * 判断是否为运算符
     * 
     * @param cur
     * @param peek
     * @return
     */

    /**
     * 利用ASCII码-40做下标去算术符号优先级
     *
     * @param cur
     * @param peek
     * @return
     */
    public boolean compare(char cur, String peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
        boolean result = false;
        // if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
        // result = true;
        // }

        if (getPriority(peek) >= getPriority(String.valueOf(cur))) {
            result = true;
        }
        return result;
    }

    /**
     * 按照给定的算术运算符做计算
     *
     * @param firstValue
     * @param secondValue
     * @param currentOp
     * @return
     */
    // private String calculate(String firstValue, String secondValue, char currentOp) {
    //     String result = "";
    //     switch (currentOp) {
    //     case '+':
    //         result = String.valueOf(ArithHelper.add(firstValue, secondValue));
    //         break;
    //     case '-':
    //         result = String.valueOf(ArithHelper.sub(firstValue, secondValue));
    //         break;
    //     case '*':
    //         result = String.valueOf(ArithHelper.mul(firstValue, secondValue));
    //         break;
    //     case '/':
    //         result = String.valueOf(ArithHelper.div(firstValue, secondValue));
    //         break;
    //     case '^':
    //         result = String.valueOf(ArithHelper.exp(Double.parseDouble(firstValue), Double.parseDouble(secondValue)));
    //     }
    //     return result;
    // }

    /**
     * 將後綴表達式轉為後綴樹
     * @param express
     * @return
     */
    public Stack<Tree> create_express_tree(Stack<String> express) {
        // List<Tree> list_tree = new LinkedList<>();
       Stack<Tree> stack_tree = new Stack<>();
        for (String oper : express) {
            if (ArithHelper.isOp(oper)) {
                Tree t = new Tree();
                Tree right = stack_tree.pop();
                Tree left;
                if (ArithHelper.is1Op(oper)){
                     left = null;
                }else {
                     left = stack_tree.pop();
                }
                t.value = oper;
                t.left = left;
                t.right = right;
                stack_tree.push(t);
            } else {
                Tree t = new Tree();
                t.value = oper;
                t.left = null;
                t.right = null;
                stack_tree.push(t);
            }

        }
        return stack_tree;
    }
}