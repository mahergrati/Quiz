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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.Models.QuestionModel;
import com.example.quiz.R;
import com.example.quiz.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    ArrayList<QuestionModel> list = new ArrayList<>();
    int count = 0;
    int position = 0;
    int score = 0;
    CountDownTimer timer;

    ActivityQuestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        resetTimer();
        timer.start();

        String setName = getIntent().getStringExtra("set");
        if (setName.equals("المستوى 1")) {
            setOne();
        } else if (setName.equals("المستوى 2")) {
            setTwo();
        } else if (setName.equals("المستوى 3")) {
            setThree();
        } else if (setName.equals("المستوى 4")) {
            setFour();
        } else if (setName.equals("المستوى 5")) {
            setFive();
        }
        Button btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher une boîte de dialogue de confirmation
                new AlertDialog.Builder(QuestionActivity.this)
                        .setTitle("Quitter le jeu")
                        .setMessage("Voulez-vous vraiment quitter le jeu? Votre progression sera perdue.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Démarrer l'activité SetsActivity
                                Intent intent = new Intent(QuestionActivity.this, SetsActivity.class);
                                startActivity(intent);
                                // Terminer l'activité actuelle
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        // Cacher tous les boutons d'options
        for (int i = 0; i < 4; i++) {
            binding.optionContainer.getChildAt(i).setVisibility(View.GONE);
        }

        // Afficher les boutons d'options en fonction du nombre de propositions de la question en cours
        int numOptions = getNumOptionsForCurrentQuestion();
        for (int i = 0; i < numOptions; i++) {
            binding.optionContainer.getChildAt(i).setVisibility(View.VISIBLE);
        }

        // Ajouter des écouteurs de clic pour les options visibles seulement
        for (int i = 0; i < numOptions; i++) {
            binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer((Button) view);
                }
            });
        }

        playAnimation(binding.question, 0, list.get(position).getQuestion());
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer != null) {
                    if (timer != null) {
                        timer.cancel();
                    }
                    timer.start();
                    binding.btnNext.setEnabled(false);
                    binding.btnNext.setAlpha(0.3f);
                    enableOption(true);
                    position++;
                    if (position == list.size()) {
                        Intent scoreIntent = new Intent(QuestionActivity.this, ScoreActivity.class);
                        scoreIntent.putExtra("score", score);
                        scoreIntent.putExtra("total", list.size());
                        startActivity(scoreIntent);
                        finish();
                        return;
                    }
                    count = 0;
                    playAnimation(binding.question, 0, list.get(position).getQuestion());

                    // Réinitialiser la visibilité des options pour la nouvelle question
                    int numOptions = getNumOptionsForCurrentQuestion();
                    for (int i = 0; i < 4; i++) {
                        if (i < numOptions) {
                            binding.optionContainer.getChildAt(i).setVisibility(View.VISIBLE);
                        } else {
                            binding.optionContainer.getChildAt(i).setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

        private void resetTimer () {
            timer = new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long l) {
                    binding.timer.setText(String.valueOf(l / 1000));
                }

                @Override
                public void onFinish() {
                    if (!isFinishing()) {
                        Dialog dialog = new Dialog(QuestionActivity.this);
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.timeout_dialog);
                        dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(QuestionActivity.this, SetsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        dialog.show();
                    }
                }

            };
        }
        private void playAnimation (View question,int value, String data){
            question.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                    .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            if (value == 0 && count < 4) {
                                String option = "";
                                if (count == 0) {
                                    option = list.get(position).getOptionA();
                                } else if (count == 1) {
                                    option = list.get(position).getOptionB();
                                } else if (count == 2) {
                                    option = list.get(position).getOptionC();
                                } else if (count == 3) {
                                    option = list.get(position).getOptionD();
                                }
                                playAnimation(binding.optionContainer.getChildAt(count), 0, option);
                                count++;
                            }
                        }

                        @Override
                        public void onAnimationEnd(@NonNull Animator animator) {
                            if (value == 0) {
                                try {
                                    ((TextView) question).setText(data);
                                    binding.totalquestion.setText(position + 1 + "/" + list.size());
                                } catch (Exception e) {
                                    ((Button) question).setText(data);
                                }
                                question.setTag(data);
                                playAnimation(question, 1, data);
                            }
                        }

                        @Override
                        public void onAnimationCancel(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(@NonNull Animator animator) {

                        }
                    });
        }

        private void enableOption ( boolean enable){
            for (int i = 0; i < 4; i++) {
                binding.optionContainer.getChildAt(i).setEnabled(enable);
                if (enable) {
                    binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
                }
            }
        }

        private void checkAnswer (Button selectedOption){
            if (timer != null) {
                timer.cancel();
            }
            binding.btnNext.setEnabled(true);
            binding.btnNext.setAlpha(1);
            if (selectedOption.getText().toString().equals(list.get(position).getCorrectAns())) {
                score++;
                selectedOption.setBackgroundResource(R.drawable.right_answer);
            } else {
                selectedOption.setBackgroundResource(R.drawable.wrong_answer);
                Button correctOption = (Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectAns());
                if (correctOption != null) {
                    correctOption.setBackgroundResource(R.drawable.right_answer);
                }
            }
        }
        private void setFive () {
            list.add(new QuestionModel("للكشف عن ثقب في العجلة؟:", "تمرير الطّوق قريبا من النّار", "وضع الطّوق في إناء مملوء ماء", "التّأمّل جيّدا في الطّوق", null, "وضع الطّوق في إناء مملوء ماء"));
            list.add(new QuestionModel("عند الضّغط على مكبس محقنة؟:", "ينتشر الهواء", "ينضغط الهواء", null, null, "ينضغط الهواء"));
            list.add(new QuestionModel("ظاهرة تحدث للهواء بفقدان الحرارة؟:", "التّقلّص", "التّمدّد", null, null, "التّقلّص"));
            list.add(new QuestionModel("يؤجّج نارا تكاد تنطفئ ويُستخدم لإنعاش المرضى؟:", "الأكسيجين", "الأزوت", null, null, "الأكسيجين"));
            list.add(new QuestionModel("غاز سام يخرج مع هواء الزّفير؟:", "ثاني أكسيد الكربون", "هباب الفحم", "الأكسيجين", null, "ثاني أكسيد الكربون"));
            list.add(new QuestionModel("عندما يبرد يكوّن الضّباب والنّدى والسّحب؟:", "بخار الماء", "الأكسيجين", null, null, "بخار الماء"));
            list.add(new QuestionModel("تتمّ عمليّة الإحتراق في الهواء بتوفّر؟:", "مصدر حرارة", "ثاني أكسيد الكربون", "أزوت", null, "مصدر حرارة"));
            list.add(new QuestionModel("تختلف سرعة الاحتراق حسب؟:", "حجم المادّة المحترقة", "نوعيّة المادّة المحترقة", null, null, "نوعيّة المادّة المحترقة"));
            list.add(new QuestionModel("لتتمّ عمليّة الاحتراق يجب توفّر؟:", "جسم قابل للاحتراق", "ثاني أكسيد الكربون", null, null, "جسم قابل للاحتراق"));
            list.add(new QuestionModel("ينتج عن عمليّة الاحتراق؟:", "ضوء", "أكسيجين", null, null, "ضوء"));

        }
        private void setFour () {
            list.add(new QuestionModel("أيّ من العناصر الثلاثة لا يتدخّل في عملية الاحتراق:", "الأكسجين، الوقود، الحرارة", "الهيدروجين، الأكسجين، الكربون", "الكربون، الأكسجين، الزئبق", null, "الكربون، الأكسجين، الزئبق"));
            list.add(new QuestionModel("ماهي العناصر الأساسية اللازمة لوقوع عملية الاحتراق؟", "الأكسجين والحرارة", "الهيدروجين والأكسجين", "الكربون والماء", null, "الأكسجين والحرارة"));
            list.add(new QuestionModel("ما الذي يتم إخراجه عند اكتمال هذه العملية؟", "يتم إخراج الأوزون", "يتم إخراج ثاني أكسيد الكربون", "يتم إخراج الماء", null, "يتم إخراج ثاني أكسيد الكربون"));
            list.add(new QuestionModel("ماهي العمليّة التي يتمّ فيها تحويل الجسم الى غاز أثناء الاحتراق", "التّبخّر", "لانصهار", "التّجمّد", null, "التّبخّر"));
            list.add(new QuestionModel("ما الذي يمنع عمليّة الاحتراق من الحدوث", "وجود الضوء", "زيادة الحرارة", "نقص أكسجين", null, "نقص أكسجين"));
            list.add(new QuestionModel("ما الذي يختلف من مادّة الى أخرى في عمليّة الاحتراق", "كمية الضوء المنتجة", "درجة الحرارة المطلوبة", "شدّة الرائحة الناتجة", null, "درجة الحرارة المطلوبة"));
            list.add(new QuestionModel("ما الذي يحتاجه الجسم ليتحوّل الى غاز خلال عمليّة الاحتراق", "الهباب", "الهواء", "الضوء", null, "الهواء"));
            list.add(new QuestionModel("ما هو العنصر الذي يعتبر أحد أسباب عدم حدوث عملية الاحتراق", "الالمنيوم", "النيتروجين", "الكربون", null, "النيتروجين"));
            list.add(new QuestionModel("ما الذي يتحوّل الى بخار الماء أثناء عمليّة الاحتراق", "الماء الساخن", "الرماد", "الهباب", null, "الهباب"));
            list.add(new QuestionModel("ما هو العنصر الذي يشتعل ليوفر مصدراً للحرارة خلال عملية الاحتراق", "الهواء", "النيتروجين", "الوقود", null, "الوقود"));

        }
        private void setThree () {
            list.add(new QuestionModel("الهواء ضروري لعمليّة الاحتراق", "نعم", "لا", null, null, "نعم"));
            list.add(new QuestionModel("التيّار الهوائي لا يساعد على عمليّة الاحتراق", "صواب", "خطأ", null, null, "خطأ"));
            list.add(new QuestionModel("المكوّن الذي يتدخّل في الاحتراق", "بخار الماء", "الأكسجين", "النيتروجين", null, "الأكسجين"));
            list.add(new QuestionModel("وضعنا شمعة مشتعلة في قارورة وأحكمنا اغلاق القارورة", "تنطفأ مباشرة", "تشتعل قليلا ثمّ تنطفئ", null, null, "تشتعل قليلا ثمّ تنطفئ"));
            list.add(new QuestionModel("يتمّ الاحتراق بأكثر تأجّج", "إذا كان في الأكسجين", "إذا كان في ثاني أكسيد الكربون", null, null, "إذا كان في الأكسجين"));
            list.add(new QuestionModel("المواد الّتي تحترق مباشرة", "شمع", "بنزين", "قماش", null, "بنزين"));
            list.add(new QuestionModel("المواد التي تحترق بعد تسخينها", "كحول", "نفط", "خشب", null, "خشب"));
            list.add(new QuestionModel("يمكن لرجل فضاء اشعال عود ثقاب على سطح الفضاء", "نعم", "لا", null, null, "لا"));
            list.add(new QuestionModel("يتمّ الاحتراق بتوفّر الهواء و:", "مصدر حرارة", "ثاني أكسيد الكربون", null, null, "مصدر حرارة"));
            list.add(new QuestionModel("نأتي بموقد ونضع أنبوبا على النّار وبداخله أعواد خشب، ماذا نلاحظ؟", "احترق الخشب وأصبح رمادا", "لم يشتعل الخشب", "اشتعل الخشب بسرعة", null, "احترق الخشب وأصبح رمادا"));


        }

        private void setTwo () {
            list.add(new QuestionModel("تبلغ نسبة الأكسجين في الهواء: ", "الخمس", "الثلث", "الخمسين", "الخمس"));
            list.add(new QuestionModel("المادّة التي تمثّل أعلى نسبة في مكوّنات الهواء هي: ", "الأزوت", "الأكسجين", "ثاني أكسيد الكربون", "الأزوت"));
            list.add(new QuestionModel("يحوي الهواء: ", "بخار الماء", "ثاني أكسيد الكربون", "هباب الفحم", " ثاني أكسيد الكربون"));
            list.add(new QuestionModel("ما هو الغاز الموجود في الهواء وهو ضروري لحياة الكائنات الحيّة؟: ", "الأكسجين", "الهيليوم", "النّيتروجين", "الأكسجين"));
            list.add(new QuestionModel("الهواء يبرد عندما يدخل جسم الإنسان: ", "نعم", "لا", "لا"));
            list.add(new QuestionModel("نتعرّف على وجود ثاني أكسيد الكربون في الهواء من خلال: ", "تعكّر ماء الجير", "احتراق الشمع", "انعكاس الضّوء", "تعكّر ماء الجير"));
            list.add(new QuestionModel("يساعد الأكسجين على: ", "الاحتراق", "الانطفاء", "الاحتراق"));
            list.add(new QuestionModel("النّيتروجين يساعد على عمليّة الاحتراق: ", "لا", "نعم", "لا"));
            list.add(new QuestionModel("أكثر الملوّثات نسبة في الهواء: ", "ثاني أكسيد الكربون", "الهيدروجين", "ثاني أكسيد الكربون"));
            list.add(new QuestionModel("يُكوّن السّحب عندما يتكثّف ولا يساعد على الاحتراق: ", "بخار الماء", "ثاني أكسيد الكربون", "بخار الماء"));
        }

        private void setOne () {
            list.add(new QuestionModel("للهواء: ", "لون", "كتلة ", "شكل خاص به", "شكل خاص به"));
            list.add(new QuestionModel("الهواء مزيج من الغازات: ", "نعم", "لا ", "نعم"));
            list.add(new QuestionModel("يمكن نقل الهواء من إناء إلى آخر:", "نعم", "لا", "لا"));
            list.add(new QuestionModel(" إذا تعرّض الهواء إلى الحرارة فإنّه:", "ينتشر", "يتمدّد", "يتقلّص", "يتمدّد"));
            list.add(new QuestionModel("إذا تقلّص الهواء:", "زادت كتلته", "نقص حجمه", "تغيّر لونه", "نقص حجمه"));
            list.add(new QuestionModel("الهواء الأثقل هو:", "الهواء البارد", "الهواء الحارّ", "الهواء البارد"));
            list.add(new QuestionModel("يتقلّص الهواء بمفعول:", "البرودة", "الحرارة ", "البرودة"));
            list.add(new QuestionModel("1ل من الهواء يزن:", "1.3غ", "1.3كغ", "1.3غ"));
            list.add(new QuestionModel("طعم الهواء مرّ:", "نعم", "لا", "لا"));
            list.add(new QuestionModel("ماذا يحدث للشمعة عند إغلاقها؟: ", "تنطفئ", "تبقى مشتعلة قليلا ثمّ تنطفئ", "تبقى مشتعلة", "تبقى مشتعلة قليلا ثمّ تنطفئ"));
        }

        // Méthode pour obtenir le nombre de propositions pour la question en cours
        private int getNumOptionsForCurrentQuestion () {
            // Récupérer la question en cours
            QuestionModel currentQuestion = list.get(position);

            // Compter le nombre de propositions non nulles et non vides
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
    }
