package com.example.scaler.services;

import com.example.scaler.exceptions.InvalidExamException;
import com.example.scaler.exceptions.InvalidLearnerException;
import com.example.scaler.models.Exam;
import com.example.scaler.models.ExamStatus;
import com.example.scaler.models.Learner;
import com.example.scaler.models.LearnerExam;
import com.example.scaler.models.LearnerQuestionResponse;
import com.example.scaler.models.Option;
import com.example.scaler.models.Question;
import com.example.scaler.repositories.ExamRepository;
import com.example.scaler.repositories.LearnerExamRepository;
import com.example.scaler.repositories.LearnerQuestionResponseRepository;
import com.example.scaler.repositories.LearnerRepository;
import com.example.scaler.repositories.OptionRepository;
import com.example.scaler.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MCQExamServiceImpl implements MCQExamService{
    @Autowired
    private LearnerRepository learnerRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private LearnerExamRepository learnerExamRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private LearnerQuestionResponseRepository learnerQuestionResponseRepository;

    @Override
    public LearnerExam startExam(Long learnerId, Long examId) throws InvalidLearnerException, InvalidExamException {
        Optional<Learner> optionalLearner = learnerRepository.findById(learnerId);
        if(optionalLearner.isEmpty()){
            throw new InvalidLearnerException("Learner Id is not valid");
        }

        Optional<Exam> optionalExam = examRepository.findById(examId);
        if(optionalExam.isEmpty()){
            throw new InvalidExamException("exam is not valid");
        }

        Optional<LearnerExam> optionalLearnerExam = learnerExamRepository.findByLearner_IdAndExam_Id(learnerId, examId);
        LearnerExam learnerExam;

        if(optionalLearnerExam.isEmpty()){
            learnerExam = new LearnerExam();
            learnerExam.setExam(optionalExam.get());
            learnerExam.setLearner(optionalLearner.get());
            learnerExam.setStatus(ExamStatus.STARTED);
            learnerExam.setStartedAt(new Date());
            learnerExam = learnerExamRepository.save(learnerExam);
        }
        else{
            learnerExam = optionalLearnerExam.get();
            if(learnerExam.getStatus().equals(ExamStatus.STARTED)){
                throw new IllegalArgumentException("Exam already started");
            }
        }

        return learnerExam;
    }

    @Override
    public LearnerExam submitExam(Long learnerId, Long examId) throws InvalidLearnerException, InvalidExamException {
        Optional<Learner> optionalLearner = learnerRepository.findById(learnerId);
        if(optionalLearner.isEmpty()){
            throw new InvalidLearnerException("Learner Id is not valid");
        }

        Optional<Exam> optionalExam = examRepository.findById(examId);
        if(optionalExam.isEmpty()){
            throw new InvalidExamException("exam is not valid");
        }

        Optional<LearnerExam> optionalLearnerExam = learnerExamRepository.findByLearner_IdAndExam_Id(learnerId, examId);
        if(optionalLearnerExam.isEmpty()){
            throw new InvalidExamException("Exam has not started yet");
        }
        LearnerExam learnerExam = optionalLearnerExam.get();

        System.out.println("Learner Exam Status: " + learnerExam.getStatus().toString());

        if(learnerExam.getStatus().equals(ExamStatus.ENDED)){
            throw new InvalidExamException("Exam has already ended hence cannot submit");
        }

        learnerExam.setEndedAt(new Date());
        learnerExam.setStatus(ExamStatus.ENDED);

        Exam exam = learnerExam.getExam();
        List<Question> questions = questionRepository.findAllByExam_Id(exam.getId());

        int score = 0;
        for(Question question: questions){
            Optional<LearnerQuestionResponse> optionalLearnerQuestionResponse = learnerQuestionResponseRepository.
                    findByQuestion_IdAndLearner_Id(question.getId(), learnerId);

            if(optionalLearnerQuestionResponse.isPresent()){
                Option option = optionalLearnerQuestionResponse.get().getOption();
                if(question.getCorrectOption().equals(option))
                {
                    score += question.getScore();
                }
            }
        }
        learnerExam.setScoreObtained(score);
        learnerExam = learnerExamRepository.save(learnerExam);
        return learnerExam;
    }

    @Override
    public LearnerQuestionResponse answerQuestion(Long learnerId, Long questionId, Long optionId) throws InvalidLearnerException, InvalidExamException {
        Optional<Learner> optionalLearner = learnerRepository.findById(learnerId);
        if(optionalLearner.isEmpty()){
            throw new InvalidLearnerException("Learner Id is not valid");
        }
        Learner learner = optionalLearner.get();

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if(optionalQuestion.isEmpty()){
            throw new InvalidExamException("question is not valid");
        }
        Question question = optionalQuestion.get();
        Exam exam = question.getExam();

        Optional<LearnerExam> optionalLearnerExam = learnerExamRepository.findByLearner_IdAndExam_Id(learnerId, exam.getId());
        if(optionalLearnerExam.isPresent() && !optionalLearnerExam.get().getStatus().equals(ExamStatus.STARTED)){
            throw new InvalidExamException("Exam needs to start before we answer a response");
        }

        Optional<Option> optionalOption = optionRepository.findById(optionId);
        if(optionalOption.isEmpty()){
            throw new InvalidExamException("Invalid option Id");
        }
        Option option = optionalOption.get();

        if(question.getOptions().stream().map(Option::getId).noneMatch(id -> id.equals(optionId))){
            throw new InvalidExamException("Option not found in question");
        }

        Optional<LearnerQuestionResponse> optionalLearnerQuestionResponse = learnerQuestionResponseRepository.
                findByQuestion_IdAndLearner_Id(questionId, learnerId);
        LearnerQuestionResponse learnerQuestionResponse;
        if(optionalLearnerQuestionResponse.isEmpty()){
            learnerQuestionResponse = new LearnerQuestionResponse();
            learnerQuestionResponse.setQuestion(question);
            learnerQuestionResponse.setLearner(learner);
        }
        else {
            learnerQuestionResponse = optionalLearnerQuestionResponse.get();
        }
        learnerQuestionResponse.setOption(option);
        learnerQuestionResponse = learnerQuestionResponseRepository.save(learnerQuestionResponse);

        return learnerQuestionResponse;
    }
}
