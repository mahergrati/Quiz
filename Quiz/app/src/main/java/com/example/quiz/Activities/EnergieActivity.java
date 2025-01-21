package com.example.quiz.Activities;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.Models.QuestionModel;
import com.example.quiz.R;
import com.example.quiz.databinding.ActivityEnergieBinding;

import java.util.ArrayList;

public class EnergieActivity extends AppCompatActivity {
    ArrayList<QuestionModel> ListEnergie = new ArrayList<>();
    int countEnergie = 0;
    int scoreEnergie = 0;
    int positionEnergie = 0;
    CountDownTimer timerEnergie;

    ActivityEnergieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnergieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        resetTimerEnergie();
        timerEnergie.start();
        String setNames = getIntent().getStringExtra("set");
        if (setNames.equals("المستوى 1")) {
            setOneE();
        } else if (setNames.equals("المستوى 2")) {
            setTwoE();
        } else if (setNames.equals("المستوى 3")) {
            setThreeE();
        } else if (setNames.equals("المستوى 4")) {
            setFourE();
        }
        Button btnquit = findViewById(R.id.btnquit);
        btnquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher une boîte de dialogue de confirmation
                new AlertDialog.Builder(EnergieActivity.this)
                        .setTitle("Quitter le jeu")
                        .setMessage("Voulez-vous vraiment quitter le jeu? Votre progression sera perdue.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Démarrer l'activité SetsActivity
                                Intent intent = new Intent(EnergieActivity.this, SetsActivityEnergie.class);
                                startActivity(intent);
                                // Terminer l'activité actuelle
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        for (int i = 0; i < 4; i++) {
            binding.optionsContainer.getChildAt(i).setVisibility(View.GONE);
        }
        // Afficher les boutons d'options en fonction du nombre de propositions de la question en cours
        int NumOptions = getNumOptionsForCurrentQuestionE();
        for (int i = 0; i < NumOptions; i++) {
            Button optionButton = (Button) binding.optionsContainer.getChildAt(i);
            if (!optionButton.getText().toString().isEmpty()) {
                optionButton.setVisibility(View.VISIBLE);
            }
        }

        // Ajouter des écouteurs de clic pour les options visibles seulement
        for (int i = 0; i < NumOptions; i++) {
            final Button optionButton = (Button) binding.optionsContainer.getChildAt(i);
            if (!optionButton.getText().toString().isEmpty()) {
                optionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkAnswer(optionButton);
                    }
                });
            }
        }

        playAnimation(binding.questionEn, 0, ListEnergie.get(positionEnergie).getQuestion());
        binding.btnnex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerEnergie != null) {
                    if (timerEnergie != null) {
                        timerEnergie.cancel();
                    }
                    timerEnergie.start();
                    binding.btnnex.setEnabled(false);
                    binding.btnnex.setAlpha(0.3f);
                    enableOption(true);
                    positionEnergie++;
                    if (positionEnergie == ListEnergie.size()) {
                        Intent scoreIntent = new Intent(EnergieActivity.this, ScoreActivity.class);
                        scoreIntent.putExtra("score", scoreEnergie);
                        scoreIntent.putExtra("total", ListEnergie.size());
                        startActivity(scoreIntent);
                        finish();
                        return;
                    }
                    countEnergie = 0;
                    playAnimation(binding.questionEn, 0, ListEnergie.get(positionEnergie).getQuestion());

                    // Réinitialiser la visibilité des options pour la nouvelle question
                    int numOptions = getNumOptionsForCurrentQuestionE();
                    for (int i = 0; i < 4; i++) {
                        if (i < numOptions) {
                            binding.optionsContainer.getChildAt(i).setVisibility(View.VISIBLE);
                        } else {
                            binding.optionsContainer.getChildAt(i).setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    private void resetTimerEnergie() {
        timerEnergie = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                binding.timerEn.setText(String.valueOf(l / 1000));
            }

            @Override
            public void onFinish() {
                if (!isFinishing()) {
                    Dialog dialog = new Dialog(EnergieActivity.this);
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.timeout_dialog);
                    dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(EnergieActivity.this, SetsActivityEnergie.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.show();
                }
            }
        };
    }

    private void playAnimation(View question, int value, String data) {
        question.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (value == 0 && countEnergie < 4) {
                    String option = "";
                    if (countEnergie == 0) {
                        option = ListEnergie.get(positionEnergie).getOptionA();
                    } else if (countEnergie == 1) {
                        option = ListEnergie.get(positionEnergie).getOptionB();
                    } else if (countEnergie == 2) {
                        option = ListEnergie.get(positionEnergie).getOptionC();
                    } else if (countEnergie == 3) {
                        option = ListEnergie.get(positionEnergie).getOptionD();
                    }
                    playAnimation(binding.optionsContainer.getChildAt(countEnergie), 0, option);
                    countEnergie++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (value == 0) {
                    try {
                        ((TextView) question).setText(data);
                        binding.totalQuestionE.setText(positionEnergie + 1 + "/" + ListEnergie.size());
                    } catch (Exception e) {
                        ((Button) question).setText(data);
                    }
                    question.setTag(data);
                    playAnimation(question, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }


    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            binding.optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable) {
                binding.optionsContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
            }
        }
    }

    private void checkAnswer(Button selectedOption) {
        if (timerEnergie != null) {
            timerEnergie.cancel();
        }
        binding.btnnex.setEnabled(true);
        binding.btnnex.setAlpha(1);
        if (selectedOption.getText().toString().equals(ListEnergie.get(positionEnergie).getCorrectAns())) {
            scoreEnergie++;
            selectedOption.setBackgroundResource(R.drawable.right_answer);
        } else {
            selectedOption.setBackgroundResource(R.drawable.wrong_answer);
            Button correctOption = (Button) binding.optionsContainer.findViewWithTag(ListEnergie.get(positionEnergie).getCorrectAns());
            if (correctOption != null) {
                correctOption.setBackgroundResource(R.drawable.right_answer);
            }
        }
    }

    private void setFourE() {
        ListEnergie.add(new QuestionModel("يوجد المغنط في:", "خمسة أشكال", "أربعة أشكال", "ثلاثة أشكال", null, "أربعة أشكال"));
        ListEnergie.add(new QuestionModel("المغنط لا يجذب الأجسام المصنوعة من:", "الورق", "الحديد", null, null, "الورق"));
        ListEnergie.add(new QuestionModel("يتساوى التّأثير المغناطيسي للمغنط في كلّ أجزائه:", "لا", "نعم", null, null, "لا"));
        ListEnergie.add(new QuestionModel("كلّ مغنط مهما كان شكله له قطبان:", "نعم", "لا", null, null, "نعم"));
        ListEnergie.add(new QuestionModel("قطبان متماثلان:", "يتجاذبان", "يتنافران", null, null, "يتنافران"));
        ListEnergie.add(new QuestionModel("قطبان مختلفان:", "يتجاذبان", "يتنافران", null, null, "يتجاذبان"));
        ListEnergie.add(new QuestionModel("تتكوّن البوصلة من:", "مرتكز", "صحن", "ميناء", null, "مرتكز"));
        ListEnergie.add(new QuestionModel("يستعمل الإنسان البوصلة في:", "تحديد الاتّجاهات", "معرفة حالة الطّقس", "معرفة شدّة الرّيّاح", null, "تحديد الاتّجاهات"));
        ListEnergie.add(new QuestionModel("من الاتجاهات الرّئيسيّة:", "شمال", "الشمال الشرقي", "الجنوب الغربي", null, "شمال"));
        ListEnergie.add(new QuestionModel("من الاتّجاهات الفرعيّة:", "الجنوب الشرقي", "شرق", "غرب", null, "جنوب شرق"));

    }

    private void setThreeE() {
        ListEnergie.add(new QuestionModel("ما الوظيفة الرئيسية للبوصلة؟", "تحديد اتجاهات الشمال والجنوب", "قياس درجة الحرارة", "في معرفة شدّة الرياح", null, "تحديد اتجاهات الشمال والجنوب"));
        ListEnergie.add(new QuestionModel("ما الذي يتحكم في حركة إبرة البوصلة؟", "المجال المغناطيسي للأرض", "الضغط الجوي", "درجة الحرارة", null, "المجال المغناطيسي للأرض"));
        ListEnergie.add(new QuestionModel("ما هو الاتجاه الذي تشير إليه الإبرة في البوصلة؟", "الشمال", "الجنوب", "الغرب", null, "الشمال"));
        ListEnergie.add(new QuestionModel("الإبرة الممغنطة توجد:", "داخل البوصلة", "خارج البوصلة", null, null, "داخل البوصلة"));
        ListEnergie.add(new QuestionModel("علبة البوصلة عبارة عن غلاف معدني", "صواب", "خطأ", null, null, "صواب"));
        ListEnergie.add(new QuestionModel("في غياب البوصلة يحدد الإنسان الاتجاهات في الليل بواسطة :", "النجوم", "البارومتر", "الديكمتر", null, "النجوم"));
        ListEnergie.add(new QuestionModel("تكون علبة البوصلة مصنوعة من:", "مادة لا تتأثر بالقوة المغناطيسية", "مادة تتأثر بالقوة المغناطيسية", "في أي مادة شئنا", null, "مادة لا تتأثر بالقوة المغناطيسية"));
        ListEnergie.add(new QuestionModel("ما هي وظيفة السهم المغناطيسي في البوصلة؟", "تحديد اتجاه الشرق", "تحديد اتجاه الغرب", " تحديد اتجاه الشمال", null, " تحديد اتجاه الشمال"));
        ListEnergie.add(new QuestionModel("ما هو السائل الذي يوجد في بعض البوصلات؟", "الماء", "الكحول", "الزيت", null, "الزيت"));
        ListEnergie.add(new QuestionModel("الزجاجة لا تغطي الأجزاء الداخلية للبوصلة", "صواب", "خطأ", null, null, "خطأ"));


    }

    private void setTwoE() {
        ListEnergie.add(new QuestionModel("يمكن الحصول على مغنط له قطب واحد", "صواب", "خطأ", null, null, "صواب"));
        ListEnergie.add(new QuestionModel("القطب الشمالي للمغنط أقوى من القطب الجنوبي", "صواب", "خطأ", null, null, "خطأ"));
        ListEnergie.add(new QuestionModel("يمكن للإنسان الاستفادة من القوّة المغناطيسيّة", "صواب", "خطأ", null, null, "صواب"));
        ListEnergie.add(new QuestionModel("ماهي الظّاهرة التي تحدث بين قطبين مختلفين", "التنافر", "التجاذب", null, null, "التجاذب"));
        ListEnergie.add(new QuestionModel("ماذا يحدث عندما يتقارب قطبين متشابهين، سواء كانا شماليين أو جنوبيين؟", "يتنافران ويدفع كل منهما الآخر بعيدًا", "يتجاذبان بقوة متساوية", null, null, "يتنافران ويدفع كل منهما الآخر بعيدًا"));
        ListEnergie.add(new QuestionModel("ما الذي يحدث لقوة التجاذب بين قطبين مغناطيسيين عندما يزداد بعدهما؟", "تزداد", "تقل", null, null, "تقل"));
        ListEnergie.add(new QuestionModel("لماذا يتنافر قطبا مغناطيس متشابهان عندما يتقاربان؟", "لتفادي التماثل في التوجيه", "بسبب تنافر الأقطاب المتشابه", null, null, "بسبب تنافر الأقطاب المتشابه"));
        ListEnergie.add(new QuestionModel("المغنط يجذب كلّ الأجسام", "نعم", "لا", null, null, "لا"));
        ListEnergie.add(new QuestionModel("المغنط له قطب واحد", "نعم", "لا", null, null, "لا"));
        ListEnergie.add(new QuestionModel("يفقد المغنط قدرته على الجذب إذا انقسم الى أجزاء", "نعم", "لا", null, null, "لا"));
    }







    private void setOneE() {
        ListEnergie.add(new QuestionModel("ما هي الظاهرة التي تحدث عندما تتأثر جسيمة بقوة مغناطيسية؟", "التحرك", "التوجيه", "الإيقاف", null, "التحرك"));
        ListEnergie.add(new QuestionModel("ماذا يسمى المغناطيس الذي يمتلك قطبين؟", "المغنط", "المغنطس", "المغناطيس", null, "المغناطيس"));
        ListEnergie.add(new QuestionModel("ما هي الطاقة التي يتم إصدارها عند تحريك المغناطيس عند بعض الحديد؟", "الطاقة الحرارية", "الطاقة الكهربائية", "الطاقة الكيميائية", null, "الطاقة الكهربائية"));
        ListEnergie.add(new QuestionModel("ما الذي يحدث لجسيمة حديدية عندما تتعرّض لقوة مغناطيسية قوية؟", "ترتفع", "تنخفض", "تتحرّك", null, "تتحرّك"));
        ListEnergie.add(new QuestionModel("ما الذي يسمى المغناطيس الذي يمتلك قطبًا واحدًا فقط؟", "المغنط", "المغنطس", "المغنطيس", null, "المغنط"));
        ListEnergie.add(new QuestionModel("ما هو الجزء الذي يتجاذب فيه المغناطيس الجسيمة؟", "القطب الشمالي", "القطب الجنوبي", "كلا الشمال والجنوب", null, "كلا الشمال والجنوب"));
        ListEnergie.add(new QuestionModel("كيف تؤثر قوة المغناطيسية على قوة جسيمة مغناطيسية أخرى؟", "تجاذبها", "تنافرها", "تثبيتها", null, "تجاذبها"));
        ListEnergie.add(new QuestionModel("ما الذي يحدث لقوة التجاذب بين مغنطين عندما يتقاربان؟", "تزداد", "تقل", "تبقى ثابتة", null, "تزداد"));
        ListEnergie.add(new QuestionModel("ما هي الظاهرة التي تحدث عندما يتقارب مغنطين متشابهين؟", "التنافر", "التجاذب", "الاحتكاك", null, "التنافر"));
        ListEnergie.add(new QuestionModel("ما هي الظاهرة التي تحدث عندما تتباعد مغنطين متشابهين؟", "التجاذب", "التنافر", "الاحتكاك", null, "التجاذب"));

    }

    private int getNumOptionsForCurrentQuestionE() {
        if (positionEnergie < ListEnergie.size()) {
            QuestionModel currentQuestion = ListEnergie.get(positionEnergie);
            int numOptions = 0;
            if (currentQuestion.getOptionA() != null && !currentQuestion.getOptionA().isEmpty()) {
                numOptions++;
            }
            if (currentQuestion.getOptionB() != null && !currentQuestion.getOptionB().isEmpty()) {
                numOptions++;
            }
            if (currentQuestion.getOptionC() != null && !currentQuestion.getOptionC().isEmpty()) {
                numOptions++;
            }
            if (currentQuestion.getOptionD() != null && !currentQuestion.getOptionD().isEmpty()) {
                numOptions++;
            }
            return numOptions;
        }
        return 0;
    }
}
