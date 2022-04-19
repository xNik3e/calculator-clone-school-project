package com.example.calculator.utils;

import android.content.Context;

import com.example.calculator.R;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EvalEquation {

    private Context context;
    private String s_expression;
    private String temp_expression;
    private Operator factorial, percentage;
    private boolean isTrigonometric = false;
    private boolean isRad = true;

    private char divide_char, multiply_char, add_char, subtract_char, pie_char, euler_char, power_char, root_char, square_char,
            percent_char, dot_char, lparent_char, rparent_char, factorial_char;
    private String sup_negative_one, s_tangent, s_sinus, s_cosine, s_exponent, s_log, s_ln, s_arcsin, s_arccos, s_arctan;

    private static Map<String, String> replacements = new HashMap<String, String>();
    private static Map<String, String> my_t_replacements = new HashMap<String, String>();

    private Function[] myFunctions = {
            new Function("my_sin") {
                public double apply(double... args) {
                    return Math.sin(Math.toDegrees(args[0]));
                }
            },
            new Function("my_cos") {
                public double apply(double... args) {
                    return Math.cos(Math.toDegrees(args[0]));
                }
            },
            new Function("my_tan") {
                public double apply(double... args) {
                    return Math.tan(Math.toDegrees(args[0]));
                }
            },
            new Function("my_asin") {
                public double apply(double... args) {
                    return Math.asin(Math.toDegrees(args[0]));
                }
            },
            new Function("my_acos") {
                public double apply(double... args) {
                    return Math.acos(Math.toDegrees(args[0]));
                }
            },
            new Function("my_atan") {
                public double apply(double... args) {
                    return Math.atan(Math.toDegrees(args[0]));
                }
            }
    };

    public EvalEquation(Context context) {
        this.context = context;
        /*
        Chars initialization
         */
        divide_char = context.getString(R.string.divide).charAt(0);
        multiply_char = context.getString(R.string.multiply).charAt(0);
        subtract_char = context.getString(R.string.minus).charAt(0);
        add_char = context.getString(R.string.plus).charAt(0);
        pie_char = context.getString(R.string.pie).charAt(0);
        euler_char = context.getString(R.string.euler).charAt(0);
        square_char = context.getString(R.string.square).charAt(0);
        power_char = context.getString(R.string.power).charAt(0);
        root_char = context.getString(R.string.root).charAt(0);
        percent_char = '%';
        dot_char = '.';
        lparent_char = '(';
        rparent_char = ')';
        sup_negative_one = context.getString(R.string.negative_power);
        s_cosine = context.getString(R.string.cosine);
        s_sinus = context.getString(R.string.sinus);
        s_tangent = context.getString(R.string.tangent);
        s_exponent = context.getString(R.string.exponent);
        s_log = context.getString(R.string.logarythm);
        s_ln = context.getString(R.string.logn);
        factorial_char = context.getString(R.string.factorial).charAt(0);
        s_arcsin = context.getString(R.string.sinus) + sup_negative_one;
        s_arccos = context.getString(R.string.cosine) + sup_negative_one;
        s_arctan = context.getString(R.string.tangent) + sup_negative_one;

        /*
        Putting values into map
         */

        replacements.put(String.valueOf(divide_char), "/");
        replacements.put(String.valueOf(multiply_char), "*");
        replacements.put(String.valueOf(subtract_char), "-");
        replacements.put(String.valueOf(add_char), "+");
        replacements.put(String.valueOf(pie_char), "pi");
        replacements.put(String.valueOf(euler_char), "e");
        replacements.put(String.valueOf(square_char), "^(2)");
        replacements.put(String.valueOf(power_char), "^");
        replacements.put(String.valueOf(root_char), "sqrt");
        replacements.put(String.valueOf(percent_char), "%");
        replacements.put(String.valueOf(dot_char), ".");
        replacements.put(String.valueOf(lparent_char), "(");
        replacements.put(String.valueOf(rparent_char), ")");
        replacements.put(s_exponent, "exp");
        replacements.put(s_log, "log10");
        replacements.put(s_ln, "log");
        replacements.put(String.valueOf(factorial_char), "!");

        replacements.put(s_sinus, "sin");
        replacements.put(s_cosine, "cos");
        replacements.put(s_tangent, "tan");
        replacements.put(s_arcsin, "asin");
        replacements.put(s_arccos, "acos");
        replacements.put(s_arctan, "atan");

        my_t_replacements.put("sin", "my_sin");
        my_t_replacements.put("cos", "my_cos");
        my_t_replacements.put("tan", "my_tan");
        my_t_replacements.put("asin", "my_asin");
        my_t_replacements.put("acos", "my_acos");
        my_t_replacements.put("atan", "my_atan");

        this.factorial = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                final int arg = (int) args[0];
                if ((double) arg != args[0]) {
                    throw new IllegalArgumentException("Operand for factorial has to be an integer");
                }
                if (arg < 0) {
                    throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
                }
                double result = 1;
                for (int i = 1; i <= arg; i++) {
                    result *= i;
                }
                return result;
            }
        };

        this.percentage = new Operator("%", 1, true, Operator.PRECEDENCE_MULTIPLICATION) {
            @Override
            public double apply(double... args) {
                final double arg = args[0];
                return arg / 100.0f;
            }
        };

    }

    public String evaluate() {
        if (validateExpression()) {
            ExpressionBuilder eb;
            //if expression contains trigonometric functions
            if (isTrigonometric && !isRad) {
                this.temp_expression = this.s_expression;
                for (Map.Entry<String, String> r : my_t_replacements.entrySet()) {
                    this.temp_expression = this.temp_expression.replace(r.getKey(), r.getValue());
                }
                eb = new ExpressionBuilder(this.temp_expression);
                eb.functions(myFunctions);
            }else{
                eb = new ExpressionBuilder(this.s_expression);
            }
            if (s_expression.indexOf(factorial_char) != -1) {
                eb.operator(factorial);
            }
            if (s_expression.indexOf(percent_char) != -1) {
                eb.operator(percentage);
            }
            Expression e = eb.build();
            double result = e.evaluate();

            return String.valueOf(result);
        } else {
            return null;
        }
    }

    public String asyncEval() throws ExecutionException, InterruptedException {
        if(validateExpression()){
            ExpressionBuilder eb;
            //if expression contains trigonometric functions
            if (isTrigonometric && !isRad) {
                this.temp_expression = this.s_expression;
                for (Map.Entry<String, String> r : my_t_replacements.entrySet()) {
                    this.temp_expression = this.temp_expression.replace(r.getKey(), r.getValue());
                }
                eb = new ExpressionBuilder(this.temp_expression);
                eb.functions(myFunctions);
            }else{
                eb = new ExpressionBuilder(this.s_expression);
            }
            if (s_expression.indexOf(factorial_char) != -1) {
                eb.operator(factorial);
            }
            if (s_expression.indexOf(percent_char) != -1) {
                eb.operator(percentage);
            }
            Expression e = eb.build();
            ExecutorService exec = Executors.newFixedThreadPool(1);
            Future<Double> future = e.evaluateAsync(exec);
            return future.get().toString();
        }else{
            return null;
        }
    }

    public void setS_expression(String expression) {
        String temp = expression;
        for (Map.Entry<String, String> r : replacements.entrySet()) {
            temp = temp.replace(r.getKey(), r.getValue());
        }
        this.s_expression = temp;
        if (s_expression.contains("sin")
                || s_expression.contains("cos")
                || s_expression.contains("tan")
                || s_expression.contains("acos")
                || s_expression.contains("asin")
                || s_expression.contains("atan")) {
            this.isTrigonometric = true;
        }
    }

    public String getS_expression() {
        return s_expression;
    }

    public boolean validateExpression() {
        ExpressionBuilder e = new ExpressionBuilder(this.s_expression);
        if (s_expression.indexOf(factorial_char) != -1) {
            e.operator(factorial);
        }
        if (s_expression.indexOf(percent_char) != -1) {
            e.operator(percentage);
        }
        Expression e_e = e.build();
        ValidationResult res = e_e.validate();
        return res.isValid();
    }

    public boolean isRad() {
        return isRad;
    }

    public void setRad(boolean rad) {
        isRad = rad;
    }
}
