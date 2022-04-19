package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.Guideline;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calculator.utils.AdvancedDrawer;
import com.example.calculator.utils.AlignTextView;
import com.example.calculator.utils.EquationFormulaAutoSize;
import com.example.calculator.utils.EvalEquation;
import com.example.calculator.utils.MeasuredScrollView;

/*
    FIXME: radian to degree conversion
    TODO: animate clearing screen
    TODO: change formula size on evaluate
*/

public class MainActivity extends AppCompatActivity {

    /*
    Everything needed for handling events
     */
    private boolean isLandscape = false;
    private boolean isExpanded = false;
    private boolean isRad = false;
    private boolean isInverted = false;
    private boolean isError = false;
    private boolean isResult = false;
    private int parenOpen = 0;
    private boolean isSpecialFunction = false;
    private int specialFunctionCount = 0;
    private String equation;
    private String resultedString;
    private float cSize1;
    private float cSize2;

    private String[] degrees;
    private char divide_char, multiply_char, add_char, subtract_char, pie_char, euler_char, power_char, root_char, square_char,
            percent_char, dot_char, lparent_char, rparent_char, factorial_char;
    private String sup_negative_one, s_tangent, s_sinus, s_cosine, s_exponent, s_log, s_ln, s_arcsin, s_arccos, s_arctan;
    /*
    Views
     */
    private TextView aboutMe;
    private View view_display;
    private View view_drawer;
    private View view_drawer_operators;
    private View view_drawer_numbers;
    private LinearLayout display;
    private TextView calc_mode;
    private MeasuredScrollView horizontalScrollView;
    private EquationFormulaAutoSize calc_formula;
    private AlignTextView calc_result;
    private AdvancedDrawer advancedDrawer;
    private ImageView btn_expand;
    private androidx.constraintlayout.widget.Guideline guideline;
    private GridLayout advancedPad;
    private ImageButton btn_toggle_degree_deg;
    private ImageButton btn_toggle_degree_rad;
    private ImageButton btn_toggle_inversion;
    private ImageButton btn_pi;
    private ImageButton btn_e;
    private ImageButton btn_fun_sin;
    private ImageButton btn_fun_arcsin;
    private ImageButton btn_fun_cos;
    private ImageButton btn_fun_arccos;
    private ImageButton btn_fun_tan;
    private ImageButton btn_fun_arctan;
    private ImageButton btn_factorial;
    private ImageButton btn_log;
    private ImageButton btn_10pow;
    private ImageButton btn_ln;
    private ImageButton btn_exp;
    private ImageButton btn_pow;
    private ImageButton btn_sqrt;
    private ImageButton btn_sqr;
    private ImageButton btn_clr;
    private ImageButton btn_del;
    private ImageButton btn_op_percent;
    private ImageButton btn_div;
    private ImageButton btn_mul;
    private ImageButton btn_sub;
    private ImageButton btn_add;
    private ImageButton btn_paren;
    private ImageButton btn_equal;
    private Button btn_dec_point;

    private EvalEquation evaluator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        /*
        Check orientation
         */

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
            isExpanded = true;
        } else
            isLandscape = false;

        evaluator = new EvalEquation(this);
        degrees = getResources().getStringArray(R.array.degrees);
        //equation = "";
        /*
        Views initialization
         */
        aboutMe = findViewById(R.id.toolbar_about);
        view_display = findViewById(R.id.display);
        view_drawer = findViewById(R.id.bottom_drawer_layout);
        view_drawer_operators = findViewById(R.id.bottom_drawer_operators);
        view_drawer_numbers = findViewById(R.id.bottom_drawer_numbers);
        
        /*
        Every view from display layout
         */
        display = (LinearLayout) findViewById(R.id.display_layout);
        calc_mode = (TextView) view_display.findViewById(R.id.mode);
        horizontalScrollView = (MeasuredScrollView) view_display.findViewById(R.id.container_formula);
        calc_formula = (EquationFormulaAutoSize) view_display.findViewById(R.id.formula);
        calc_result = (AlignTextView) view_display.findViewById(R.id.result);


        /*
        Every view from bottom drawers
         */

        //All advanced views

        if (isLandscape) {
            advancedPad = (GridLayout) view_drawer_operators.findViewById(R.id.advanced_pad);
            btn_toggle_degree_deg = (ImageButton) view_drawer_operators.findViewById(R.id.toggle_degree_deg);
            btn_toggle_degree_rad = (ImageButton) view_drawer_operators.findViewById(R.id.toggle_degree_rad);
            btn_toggle_inversion = (ImageButton) view_drawer_operators.findViewById(R.id.toggle_inv);
            btn_pi = (ImageButton) view_drawer_operators.findViewById(R.id.btn_pi);
            btn_e = (ImageButton) view_drawer_operators.findViewById(R.id.btn_e);
            btn_fun_sin = (ImageButton) view_drawer_operators.findViewById(R.id.btn_fun_sin);
            btn_fun_arcsin = (ImageButton) view_drawer_operators.findViewById(R.id.btn_fun_arcsin);
            btn_fun_cos = (ImageButton) view_drawer_operators.findViewById(R.id.btn_fun_cos);
            btn_fun_arccos = (ImageButton) view_drawer_operators.findViewById(R.id.btn_fun_arccos);
            btn_fun_tan = (ImageButton) view_drawer_operators.findViewById(R.id.btn_fun_tan);
            btn_fun_arctan = (ImageButton) view_drawer_operators.findViewById(R.id.btn_fun_arctan);
            btn_factorial = (ImageButton) view_drawer_operators.findViewById(R.id.btn_factorial);
            btn_log = (ImageButton) view_drawer_operators.findViewById(R.id.btn_log);
            btn_10pow = (ImageButton) view_drawer_operators.findViewById(R.id.btn_10pow);
            btn_ln = (ImageButton) view_drawer_operators.findViewById(R.id.btn_ln);
            btn_exp = (ImageButton) view_drawer_operators.findViewById(R.id.btn_exp);
            btn_pow = (ImageButton) view_drawer_operators.findViewById(R.id.btn_pow);
            btn_sqrt = (ImageButton) view_drawer_operators.findViewById(R.id.btn_sqrt);
            btn_sqr = (ImageButton) view_drawer_operators.findViewById(R.id.btn_sqr);

            //All standard views

            btn_clr = (ImageButton) view_drawer_numbers.findViewById(R.id.btn_clr);
            btn_del = (ImageButton) view_drawer_numbers.findViewById(R.id.btn_del);
            btn_op_percent = (ImageButton) view_drawer_numbers.findViewById(R.id.btn_op_percent);
            btn_div = (ImageButton) view_drawer_numbers.findViewById(R.id.btn_div);
            btn_mul = (ImageButton) view_drawer_numbers.findViewById(R.id.btn_mul);
            btn_sub = (ImageButton) view_drawer_numbers.findViewById(R.id.btn_sub);
            btn_add = (ImageButton) view_drawer_numbers.findViewById(R.id.btn_add);
            btn_paren = (ImageButton) view_drawer_numbers.findViewById(R.id.btn_paren);
            btn_equal = (ImageButton) view_drawer_numbers.findViewById(R.id.equal);
            btn_dec_point = (Button) view_drawer_numbers.findViewById(R.id.dec_point);

        } else {
            advancedDrawer = (AdvancedDrawer) view_drawer.findViewById(R.id.expandableAdvancedDrawer);
            btn_expand = (ImageView) view_drawer.findViewById(R.id.imgExpandView);
            guideline = (androidx.constraintlayout.widget.Guideline) view_drawer.findViewById(R.id.hgd20);
            advancedPad = (GridLayout) view_drawer.findViewById(R.id.advanced_pad);
            btn_toggle_degree_deg = (ImageButton) view_drawer.findViewById(R.id.toggle_degree_deg);
            btn_toggle_degree_rad = (ImageButton) view_drawer.findViewById(R.id.toggle_degree_rad);
            btn_toggle_inversion = (ImageButton) view_drawer.findViewById(R.id.toggle_inv);
            btn_pi = (ImageButton) view_drawer.findViewById(R.id.btn_pi);
            btn_e = (ImageButton) view_drawer.findViewById(R.id.btn_e);
            btn_fun_sin = (ImageButton) view_drawer.findViewById(R.id.btn_fun_sin);
            btn_fun_arcsin = (ImageButton) view_drawer.findViewById(R.id.btn_fun_arcsin);
            btn_fun_cos = (ImageButton) view_drawer.findViewById(R.id.btn_fun_cos);
            btn_fun_arccos = (ImageButton) view_drawer.findViewById(R.id.btn_fun_arccos);
            btn_fun_tan = (ImageButton) view_drawer.findViewById(R.id.btn_fun_tan);
            btn_fun_arctan = (ImageButton) view_drawer.findViewById(R.id.btn_fun_arctan);
            btn_factorial = (ImageButton) view_drawer.findViewById(R.id.btn_factorial);
            btn_log = (ImageButton) view_drawer.findViewById(R.id.btn_log);
            btn_10pow = (ImageButton) view_drawer.findViewById(R.id.btn_10pow);
            btn_ln = (ImageButton) view_drawer.findViewById(R.id.btn_ln);
            btn_exp = (ImageButton) view_drawer.findViewById(R.id.btn_exp);
            btn_pow = (ImageButton) view_drawer.findViewById(R.id.btn_pow);
            btn_sqrt = (ImageButton) view_drawer.findViewById(R.id.btn_sqrt);
            btn_sqr = (ImageButton) view_drawer.findViewById(R.id.btn_sqr);

            //All standard views

            btn_clr = (ImageButton) view_drawer.findViewById(R.id.btn_clr);
            btn_del = (ImageButton) view_drawer.findViewById(R.id.btn_del);
            btn_op_percent = (ImageButton) view_drawer.findViewById(R.id.btn_op_percent);
            btn_div = (ImageButton) view_drawer.findViewById(R.id.btn_div);
            btn_mul = (ImageButton) view_drawer.findViewById(R.id.btn_mul);
            btn_sub = (ImageButton) view_drawer.findViewById(R.id.btn_sub);
            btn_add = (ImageButton) view_drawer.findViewById(R.id.btn_add);
            btn_paren = (ImageButton) view_drawer.findViewById(R.id.btn_paren);
            btn_equal = (ImageButton) view_drawer.findViewById(R.id.equal);
            btn_dec_point = (Button) view_drawer.findViewById(R.id.dec_point);
        }
        /*
        Get savedInstance
         */

        if (savedInstanceState != null) {
            isExpanded = savedInstanceState.getBoolean("isExpanded");
            isRad = savedInstanceState.getBoolean("isRad");
            isInverted = savedInstanceState.getBoolean("isInverted");
            isError = savedInstanceState.getBoolean("isError");
            isResult = savedInstanceState.getBoolean("isResult");
            parenOpen = savedInstanceState.getInt("parenOpen");
            isSpecialFunction = savedInstanceState.getBoolean("isSpecialFunction");
            specialFunctionCount = savedInstanceState.getInt("specialFunctionCount");
            equation = savedInstanceState.getString("equation");
            resultedString = savedInstanceState.getString("resultedString");
            cSize1 = savedInstanceState.getFloat("cSize1");
            cSize2 = savedInstanceState.getFloat("cSize2");

            calc_formula.setText(equation);
            if (resultedString != null && !resultedString.isEmpty())
                calc_result.setText(resultedString);
            else {
                evaluator.setS_expression(calc_formula.getText().toString());
                evaluator.setRad(isRad);
                try {
                    String text = evaluator.asyncEval();
                    calc_result.setText(text);
                } catch (Exception e) {
                    calc_result.setText(null);
                }
            }

            if (isResult) {
                calc_formula.setTextColor(getResources().getColor(R.color.resultColor));
                calc_result.setTextColor(getResources().getColor(R.color.white));

                if (isLandscape)
                    calc_result.setTextSize(32);
                else
                    calc_result.setTextSize(72);
            }

            if (isError) {
                calc_result.setTextColor(getResources().getColor(R.color.error));
                calc_formula.setTextColor(getResources().getColor(R.color.error));
            }

            if(isExpanded && !isLandscape){
                ViewGroup.LayoutParams params = advancedDrawer.getLayoutParams();
                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180,
                        getResources().getDisplayMetrics());
                advancedDrawer.setLayoutParams(params);
                guideline.setGuidelinePercent(0.08f);
            }
            if(!isExpanded && !isLandscape){
                ViewGroup.LayoutParams params = advancedDrawer.getLayoutParams();
                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27,
                        getResources().getDisplayMetrics());
                advancedDrawer.setLayoutParams(params);
                guideline.setGuidelinePercent(0.01f);
            }
        }

        if (!isLandscape) {
            /*
        Animation for advanced drawer
         */

            btn_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded) {
                        changeState(v, advancedDrawer, guideline, 180, 27, 0.01f, 0.08f);
                    } else {
                        changeState(v, advancedDrawer, guideline, 27, 180, 0.08f, 0.01f);
                    }
                    isExpanded = !isExpanded;
                    if (isExpanded) {
                        calc_mode.setVisibility(View.VISIBLE);
                        checkDegree();
                        checkInverted();
                    } else
                        calc_mode.setVisibility(View.INVISIBLE);
                }
            });

        /*
         Checking if advance drawer is expanded and handling the inversion button
         */
            if (isExpanded) {
                checkDegree();
                checkInverted();
            }
        }

        /*
        Chars initialization
         */

        divide_char = getString(R.string.divide).charAt(0);
        multiply_char = getString(R.string.multiply).charAt(0);
        subtract_char = getString(R.string.minus).charAt(0);
        add_char = getString(R.string.plus).charAt(0);
        pie_char = getString(R.string.pie).charAt(0);
        euler_char = getString(R.string.euler).charAt(0);
        square_char = getString(R.string.square).charAt(0);
        power_char = getString(R.string.power).charAt(0);
        root_char = getString(R.string.root).charAt(0);
        percent_char = '%';
        dot_char = '.';
        lparent_char = '(';
        rparent_char = ')';
        sup_negative_one = getString(R.string.negative_power);
        s_cosine = getString(R.string.cosine);
        s_sinus = getString(R.string.sinus);
        s_tangent = getString(R.string.tangent);
        s_exponent = getString(R.string.exponent);
        s_log = getString(R.string.logarythm);
        s_ln = getString(R.string.logn);
        factorial_char = getString(R.string.factorial).charAt(0);
        s_arcsin = getString(R.string.sinus) + sup_negative_one;
        s_arccos = getString(R.string.cosine) + sup_negative_one;
        s_arctan = getString(R.string.tangent) + sup_negative_one;

        /*
        Adding textwatcher for equation
         */

        calc_formula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                evaluator.setS_expression(calc_formula.getText().toString());
                evaluator.setRad(isRad);
                try {
                    String text = evaluator.asyncEval();
                    calc_result.setText(text);
                    resultedString = text;
                } catch (Exception e) {
                    calc_result.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*
        Operation buttons handling
         */

        aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutMe.class));
            }
        });

        btn_clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                calc_formula.setText(null);
                calc_result.setText(null);
                equation = null;
                resultedString = null;
                parenOpen = 0;
                isSpecialFunction = false;
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                if (isResult) {
                    isResult = false;
                }
                if (!calc_formula.getText().toString().isEmpty()) {
                    String temp = calc_formula.getText().toString();
                    int index = temp.length() - 1;
                    StringBuffer sb = new StringBuffer(temp);
                    if (Character.isDigit(temp.charAt(index))
                            || temp.charAt(index) == percent_char
                            || temp.charAt(index) == dot_char
                            || temp.charAt(index) == lparent_char
                            || temp.charAt(index) == rparent_char
                            || temp.charAt(index) == pie_char
                            || temp.charAt(index) == euler_char
                            || temp.charAt(index) == factorial_char
                            || temp.charAt(index) == power_char
                            //|| temp.charAt(index) == root_char
                            || temp.charAt(index) == square_char
                    ) {
                        if (temp.charAt(index) == lparent_char)
                            parenOpen--;
                        if (temp.charAt(index) == rparent_char)
                            parenOpen++;
                        sb.deleteCharAt(index);
                        temp = sb.toString();
                        if (isSpecialFunction) {
                            if (temp.contains(s_sinus)) {
                                if (temp.indexOf(s_sinus) == (temp.length() - s_sinus.length())) {
                                    sb.delete(temp.indexOf(s_sinus), temp.length());
                                    specialFunctionCount--;
                                    if (specialFunctionCount == 0)
                                        isSpecialFunction = false;
                                    temp = sb.toString();
                                    calc_formula.setText(temp);
                                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                    return;
                                }
                            }
                            if (temp.contains(s_cosine)) {
                                if (temp.indexOf(s_cosine) == (temp.length() - s_cosine.length())) {
                                    sb.delete(temp.indexOf(s_cosine), temp.length());
                                    specialFunctionCount--;
                                    if (specialFunctionCount == 0)
                                        isSpecialFunction = false;
                                    temp = sb.toString();
                                    calc_formula.setText(temp);
                                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                    return;
                                }
                            }
                            if (temp.contains(s_tangent)) {
                                if (temp.indexOf(s_tangent) == (temp.length() - s_tangent.length())) {
                                    sb.delete(temp.indexOf(s_tangent), temp.length());
                                    specialFunctionCount--;
                                    if (specialFunctionCount == 0)
                                        isSpecialFunction = false;
                                    temp = sb.toString();
                                    calc_formula.setText(temp);
                                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                    return;
                                }
                            }
                            if (temp.contains(s_log)) {
                                if (temp.indexOf(s_log) == (temp.length() - s_log.length())) {
                                    sb.delete(temp.indexOf(s_log), temp.length());
                                    specialFunctionCount--;
                                    if (specialFunctionCount == 0)
                                        isSpecialFunction = false;
                                    temp = sb.toString();
                                    calc_formula.setText(temp);
                                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                    return;
                                }
                            }
                            if (temp.contains(s_ln)) {
                                if (temp.indexOf(s_ln) == (temp.length() - s_ln.length())) {
                                    sb.delete(temp.indexOf(s_ln), temp.length());
                                    specialFunctionCount--;
                                    if (specialFunctionCount == 0)
                                        isSpecialFunction = false;
                                    temp = sb.toString();
                                    calc_formula.setText(temp);
                                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                    return;
                                }
                            }
                            if (temp.contains(s_arcsin)) {
                                if (temp.indexOf(s_arcsin) == (temp.length() - s_arcsin.length())) {
                                    sb.delete(temp.indexOf(s_arcsin), temp.length());
                                    specialFunctionCount--;
                                    if (specialFunctionCount == 0)
                                        isSpecialFunction = false;
                                    temp = sb.toString();
                                    calc_formula.setText(temp);
                                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                    return;
                                }
                            }
                            if (temp.contains(s_arccos)) {
                                if (temp.indexOf(s_arccos) == (temp.length() - s_arccos.length())) {
                                    sb.delete(temp.indexOf(s_arccos), temp.length());
                                    specialFunctionCount--;
                                    if (specialFunctionCount == 0)
                                        isSpecialFunction = false;
                                    temp = sb.toString();
                                    calc_formula.setText(temp);
                                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                    return;
                                }
                            }
                            if (temp.contains(s_arctan)) {
                                if (temp.indexOf(s_arctan) == (temp.length() - s_arctan.length())) {
                                    sb.delete(temp.indexOf(s_arctan), temp.length());
                                    specialFunctionCount--;
                                    if (specialFunctionCount == 0)
                                        isSpecialFunction = false;
                                    temp = sb.toString();
                                    calc_formula.setText(temp);
                                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                    return;
                                }
                            }
                            if (temp.contains(s_exponent)) {
                                if (temp.indexOf(s_exponent) == (temp.length() - s_exponent.length())) {
                                    sb.delete(temp.indexOf(s_exponent), temp.length());
                                    specialFunctionCount--;
                                    if (specialFunctionCount == 0)
                                        isSpecialFunction = false;
                                    temp = sb.toString();
                                    calc_formula.setText(temp);
                                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                    return;
                                }
                            }
                            if (temp.indexOf(root_char) == temp.length() - 1) {
                                sb.deleteCharAt(temp.length() - 1);
                                specialFunctionCount--;
                                if (specialFunctionCount == 0)
                                    isSpecialFunction = false;
                                temp = sb.toString();
                                calc_formula.setText(temp);
                                Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                                return;

                            }

                        }
                        calc_formula.setText(temp);
                    } else {
                        while (!Character.isDigit(temp.charAt(index))) {
                            sb.deleteCharAt(index);
                            index--;
                            temp = sb.toString();
                        }
                        calc_formula.setText(temp);
                    }
                    Log.d("CHECKING_PARENT", String.valueOf(parenOpen));
                }

            }
        });

        btn_op_percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                if (isResult) {
                    isResult = false;
                    calc_formula.setText(resultedString);
                }
                calc_formula.setText(calc_formula.getText().toString() + percent_char);
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_dec_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                String temp = calc_formula.getText().toString();
                int index = temp.length() - 1;

                if (temp.charAt(index) != dot_char) {
                    temp += dot_char;
                    calc_formula.setText(temp);
                }
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                if (isResult) {
                    isResult = false;
                    calc_formula.setText(resultedString);
                }
                if (!calc_formula.getText().toString().isEmpty()) {
                    String temp = calc_formula.getText().toString();
                    int index = temp.length() - 1;

                    if (temp.charAt(index) != divide_char
                            && temp.charAt(index) != multiply_char
                            && temp.charAt(index) != subtract_char
                            && temp.charAt(index) != add_char
                            && temp.charAt(index) != power_char) {
                        temp += divide_char;
                    } else {
                        StringBuffer sb = new StringBuffer(temp);
                        if (sb.charAt(index) == subtract_char && index > 2 && sb.charAt(index - 1) == divide_char) {
                            sb.deleteCharAt(index);
                            sb.deleteCharAt(index - 1);
                        } else {
                            sb.deleteCharAt(index);
                        }
                        sb.append(divide_char);
                        temp = sb.toString();
                    }
                    calc_formula.setText(temp);
                    horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        });

        btn_mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                if (isResult) {
                    isResult = false;
                    calc_formula.setText(resultedString);
                }
                if (!calc_formula.getText().toString().isEmpty()) {
                    String temp = calc_formula.getText().toString();
                    int index = temp.length() - 1;

                    if (temp.charAt(index) != divide_char
                            && temp.charAt(index) != multiply_char
                            && temp.charAt(index) != subtract_char
                            && temp.charAt(index) != add_char
                            && temp.charAt(index) != power_char) {
                        temp += multiply_char;
                    } else {
                        StringBuffer sb = new StringBuffer(temp);
                        if (sb.charAt(index) == subtract_char && index > 2 && sb.charAt(index - 1) == divide_char) {
                            sb.deleteCharAt(index);
                            sb.deleteCharAt(index - 1);
                        } else {
                            sb.deleteCharAt(index);
                        }
                        sb.append(multiply_char);
                        temp = sb.toString();
                    }
                    calc_formula.setText(temp);
                    horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                if (isResult) {
                    isResult = false;
                    calc_formula.setText(resultedString);
                }
                if (!calc_formula.getText().toString().isEmpty()) {
                    String temp = calc_formula.getText().toString();
                    int index = temp.length() - 1;

                    if (temp.charAt(index) != divide_char
                            && temp.charAt(index) != multiply_char
                            && temp.charAt(index) != subtract_char
                            && temp.charAt(index) != add_char
                            && temp.charAt(index) != power_char) {
                        temp += add_char;
                    } else {
                        StringBuffer sb = new StringBuffer(temp);
                        if (sb.charAt(index) == subtract_char && index > 2 && sb.charAt(index - 1) == divide_char) {
                            sb.deleteCharAt(index);
                            sb.deleteCharAt(index - 1);
                        } else {
                            sb.deleteCharAt(index);
                        }
                        sb.append(add_char);
                        temp = sb.toString();
                    }
                    calc_formula.setText(temp);
                    horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        });

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                if (isResult) {
                    isResult = false;
                    calc_formula.setText(resultedString);
                }
                if (!calc_formula.getText().toString().isEmpty()) {
                    String temp = calc_formula.getText().toString();
                    int index = temp.length() - 1;

                    if (temp.charAt(index) != multiply_char
                            && temp.charAt(index) != subtract_char
                            && temp.charAt(index) != add_char
                            && temp.charAt(index) != power_char) {
                        temp += subtract_char;
                    } else {
                        StringBuffer sb = new StringBuffer(temp);
                        sb.deleteCharAt(index);
                        sb.append(subtract_char);
                        temp = sb.toString();
                    }
                    calc_formula.setText(temp);
                } else {
                    calc_formula.setText(calc_formula.getText().toString() + subtract_char);
                }
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_paren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                if (!calc_formula.getText().toString().isEmpty()) {
                    String temp = calc_formula.getText().toString();
                    int index = temp.length() - 1;
                    if (temp.charAt(index) == lparent_char
                            || (parenOpen == 0 && temp.charAt(index) != rparent_char)) {
                        temp += lparent_char;
                        parenOpen++;
                    } else if ((temp.charAt(index) == lparent_char && parenOpen > 0)
                            || (Character.isDigit(temp.charAt(index)) && parenOpen >= 0)) {
                        temp += rparent_char;
                        parenOpen--;
                    } else {
                        if (temp.charAt(index) == rparent_char && parenOpen == 0) {
                            //Do nothing
                        } else if (temp.charAt(index) == rparent_char && parenOpen != 0) {
                            temp += rparent_char;
                            parenOpen--;
                        } else {
                            temp += lparent_char;
                            parenOpen++;
                        }
                    }
                    calc_formula.setText(temp);

                } else {
                    calc_formula.setText(calc_formula.getText().toString() + lparent_char);
                    parenOpen++;
                }
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_pi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + pie_char);
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + euler_char);
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_fun_sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + s_sinus + "(");
                parenOpen++;
                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_fun_cos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + s_cosine + "(");
                parenOpen++;
                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_fun_tan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + s_tangent + "(");
                parenOpen++;
                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_fun_arcsin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + s_arcsin + "(");
                parenOpen++;
                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_fun_arccos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + s_arccos + "(");
                parenOpen++;
                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_fun_arctan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + s_arctan + "(");
                parenOpen++;
                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + s_log + "(");
                parenOpen++;
                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + s_ln + "(");
                parenOpen++;

                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + s_exponent + "(");
                parenOpen++;
                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_factorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                if (isResult) {
                    isResult = false;
                    calc_formula.setText(resultedString);
                }
                calc_formula.setText(calc_formula.getText().toString() + factorial_char);
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_pow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                if (isResult) {
                    isResult = false;
                    calc_formula.setText(resultedString);
                }
                if (!calc_formula.getText().toString().isEmpty()) {
                    String temp = calc_formula.getText().toString();
                    int index = temp.length() - 1;

                    if (temp.charAt(index) != divide_char
                            && temp.charAt(index) != multiply_char
                            && temp.charAt(index) != subtract_char
                            && temp.charAt(index) != add_char
                            && temp.charAt(index) != power_char) {
                        temp += power_char;
                    } else {
                        StringBuffer sb = new StringBuffer(temp);
                        if (sb.charAt(index) == subtract_char && index > 2 && sb.charAt(index - 1) == divide_char) {
                            sb.deleteCharAt(index);
                            sb.deleteCharAt(index - 1);
                        } else {
                            sb.deleteCharAt(index);
                        }
                        sb.append(power_char);
                        temp = sb.toString();
                    }
                    calc_formula.setText(temp);
                    horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }
        });

        btn_10pow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + "10" + power_char);
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
        btn_sqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearInput();
                calc_formula.setText(calc_formula.getText().toString() + root_char + "(");
                parenOpen++;
                if (!isSpecialFunction)
                    isSpecialFunction = true;
                specialFunctionCount++;
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_sqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError)
                    clearOutput();
                clearColor();
                calc_formula.setText(calc_formula.getText().toString() + square_char);
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        btn_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!calc_formula.getText().toString().isEmpty() && !isResult) {
                    evaluator.setS_expression(calc_formula.getText().toString());
                    evaluator.setRad(isRad);
                    try {
                        String result = evaluator.evaluate();
                        Log.e("EXCEPTION", result);
                        calc_result.setText(result);
                        resultedString = result;
                        initiateValueFlip(calc_formula.getText().toString(), resultedString);
                    } catch (Exception e) {
                        isError = true;
                        Log.e("EXCEPTION", e.getMessage());
                        calc_formula.setTextColor(getResources().getColor(R.color.error));
                        if (!e.getMessage().equals("println needs a message"))
                            calc_result.setText(e.getMessage());
                        else
                            calc_result.setText("Bad expression");
                        calc_result.setTextColor(getResources().getColor(R.color.error));
                        resultedString = calc_result.getText().toString();
                    }
                }
            }
        });
    }

    private void initiateValueFlip(String formulaString, String rString) {
        isResult = true;
        equation = formulaString;
        this.resultedString = rString;

        animateColor(calc_formula, getResources().getColor(R.color.white), getResources().getColor(R.color.resultColor));
        animateColor(calc_result, getResources().getColor(R.color.resultColor), getResources().getColor(R.color.white));

        float endSize1;
        float endSize2;

        if (isLandscape) {
            endSize1 = 16;
            endSize2 = 32;
        } else {
            endSize1 = 32;
            endSize2 = 72;
        }

        ValueAnimator animator1 = ObjectAnimator.ofFloat(calc_result, "textSize", endSize1, endSize2);
        ValueAnimator animator2 = ObjectAnimator.ofFloat(calc_formula, "textSize", endSize2, endSize1);
        animator1.setDuration(800);
        animator2.setDuration(800);
        animator1.start();
        animator2.start();
    }

    /*
    Handling numeric button presses and scrolling
    */

    public void clearOutput() {
        isError = !isError;
        calc_formula.setTextColor(getResources().getColor(R.color.white));
        calc_result.setTextColor(getResources().getColor(R.color.resultColor));
        calc_result.setText(null);
    }

    public void addNumber(View view) {
        /*
        Logging purposes only for debugging
         */
        clearInput();
        StringBuilder sb = new StringBuilder();
        sb.append("calc_formula length: ").append(calc_formula.length()).append('\n');
        sb.append("calc_formula equation string: ").append(calc_formula.getText()).append('\n');
        Log.d("Test", sb.toString());
        /*
        Actual code
         */
        if (isError)
            clearOutput();
        Button test = (Button) view;
        if (!calc_formula.getText().toString().equals("0") &&
                (calc_formula.getText().length() != 0)) {
            equation = calc_formula.getText().toString() + test.getText().toString();
        } else {
            equation = test.getText().toString();
        }
        calc_formula.setText(equation);
        horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
    }


    /*
    Additional helper methods
     */

    private void clearInput() {
        if (isResult) {
            calc_formula.setText(null);
            isResult = false;
        }
        clearColor();
    }

    private void clearColor() {
        calc_formula.setTextColor(getResources().getColor(R.color.white));
        calc_result.setTextColor(getResources().getColor(R.color.resultColor));

        if (isLandscape)
            calc_result.setTextSize(16);
        else
            calc_result.setTextSize(32);
    }

    private void animateColor(View view, int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                TextView textView = (TextView) view;
                textView.setTextColor((Integer) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    private void changeState(View v, AdvancedDrawer advancedDrawer, Guideline guideline, int from, int to, float gd_from, float gd_to) {
        ValueAnimator gdAnimator;
        ValueAnimator animator;
        animator = ValueAnimator.ofInt(from, to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = advancedDrawer.getLayoutParams();
                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val,
                        v.getContext().getResources().getDisplayMetrics());
                advancedDrawer.setLayoutParams(params);
            }
        });
        animator.setDuration(250);
        animator.start();

        gdAnimator = ValueAnimator.ofFloat(gd_from, gd_to);
        gdAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                guideline.setGuidelinePercent(val);
            }
        });
        gdAnimator.setDuration(250);
        gdAnimator.start();
    }

    private void checkInverted() {
        Drawable SVG;
        if (isInverted) {
            SVG = getResources().getDrawable(R.drawable.ic_inv_inverted);
            btn_toggle_inversion.setImageDrawable(SVG);
            //Disable buttons
            btn_fun_sin.setVisibility(View.GONE);
            btn_fun_cos.setVisibility(View.GONE);
            btn_fun_tan.setVisibility(View.GONE);
            btn_log.setVisibility(View.GONE);
            btn_ln.setVisibility(View.GONE);
            btn_sqrt.setVisibility(View.GONE);
            //Enable buttons
            btn_fun_arcsin.setVisibility(View.VISIBLE);
            btn_fun_arccos.setVisibility(View.VISIBLE);
            btn_fun_arctan.setVisibility(View.VISIBLE);
            btn_10pow.setVisibility(View.VISIBLE);
            btn_exp.setVisibility(View.VISIBLE);
            btn_sqr.setVisibility(View.VISIBLE);
        } else {
            SVG = getResources().getDrawable(R.drawable.ic_inv);
            btn_toggle_inversion.setImageDrawable(SVG);
            //Disable buttons
            btn_fun_sin.setVisibility(View.VISIBLE);
            btn_fun_cos.setVisibility(View.VISIBLE);
            btn_fun_tan.setVisibility(View.VISIBLE);
            btn_log.setVisibility(View.VISIBLE);
            btn_ln.setVisibility(View.VISIBLE);
            btn_sqrt.setVisibility(View.VISIBLE);
            //Enable buttons
            btn_fun_arcsin.setVisibility(View.GONE);
            btn_fun_arccos.setVisibility(View.GONE);
            btn_fun_arctan.setVisibility(View.GONE);
            btn_10pow.setVisibility(View.GONE);
            btn_exp.setVisibility(View.GONE);
            btn_sqr.setVisibility(View.GONE);
        }
    }

    private void checkDegree() {
        if (isRad) {
            calc_mode.setText(degrees[0]);
            btn_toggle_degree_deg.setVisibility(View.GONE);
            btn_toggle_degree_rad.setVisibility(View.VISIBLE);
        } else {
            calc_mode.setText(degrees[1]);
            btn_toggle_degree_deg.setVisibility(View.VISIBLE);
            btn_toggle_degree_rad.setVisibility(View.GONE);
        }
    }

    public void changeDegree(View view) {
        isRad = !isRad;
        checkDegree();
        evaluator.setRad(isRad);
    }

    public void changeInversion(View view) {
        isInverted = !isInverted;
        checkInverted();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isExpanded", isExpanded);
        outState.putBoolean("isRad", isRad);
        outState.putBoolean("isInverted", isInverted);
        outState.putBoolean("isError", isError);
        outState.putBoolean("isResult", isResult);
        outState.putBoolean("isSpecialFunction", isSpecialFunction);
        outState.putInt("parenOpen", parenOpen);
        outState.putInt("specialFunctionCount", specialFunctionCount);
        outState.putString("equation", calc_formula.getText().toString());
        outState.putString("resultedString", resultedString);
        outState.putFloat("cSize1", cSize1);
        outState.putFloat("cSize2", cSize2);
    }


}
