package com.api.ecnomia_api.service;

import com.api.ecnomia_api.dto.*;
import com.api.ecnomia_api.entity.*;
import com.api.ecnomia_api.exception.BusinessException;
import com.api.ecnomia_api.exception.ResourceNotFoundException;
import com.api.ecnomia_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AlternativeRepository alternativeRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserAnswerRepository userAnswerRepository;

    public List<QuizResponse> findAll(boolean apenasPublicados) {
        if (apenasPublicados) {
            return quizRepository.findByPublicadoTrue().stream()
                .map(QuizResponse::fromEntitySemRespostas)
                .toList();
        }
        return quizRepository.findAll().stream()
            .map(QuizResponse::fromEntity)
            .toList();
    }

    public QuizResponse findById(Long id, boolean showAnswers) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz não encontrado"));

        if (showAnswers) {
            return QuizResponse.fromEntity(quiz);
        }
        return QuizResponse.fromEntitySemRespostas(quiz);
    }

    public QuizResponse create(QuizRequest request, User criador) {
        Quiz quiz = Quiz.builder()
            .titulo(request.titulo())
            .descricao(request.descricao())
            .criador(criador)
            .build();

        quiz = quizRepository.save(quiz);
        return QuizResponse.fromEntity(quiz);
    }

    public QuizResponse publicar(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz não encontrado"));

        if (quiz.getPerguntas().isEmpty()) {
            throw new BusinessException("Não é possível publicar um quiz sem perguntas");
        }

        for (Question pergunta : quiz.getPerguntas()) {
            boolean hasCorreta = pergunta.getAlternativas().stream().anyMatch(Alternative::isCorreta);
            if (!hasCorreta) {
                throw new BusinessException("Todas as perguntas precisam de pelo menos uma alternativa correta");
            }
        }

        quiz.setPublicado(true);
        quiz = quizRepository.save(quiz);
        return QuizResponse.fromEntity(quiz);
    }

    public QuestionResponse addQuestion(Long quizId, QuestionRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz não encontrado"));

        Question question = Question.builder()
            .enunciado(request.enunciado())
            .pontuacao(request.pontuacao())
            .quiz(quiz)
            .build();

        question = questionRepository.save(question);
        return QuestionResponse.fromEntity(question);
    }

    public void removeQuestion(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pergunta não encontrada"));
        questionRepository.delete(question);
    }

    public AlternativeResponse addAlternative(Long perguntaId, AlternativeRequest request) {
        Question question = questionRepository.findById(perguntaId)
            .orElseThrow(() -> new ResourceNotFoundException("Pergunta não encontrada"));

        Alternative alternative = Alternative.builder()
            .texto(request.texto())
            .correta(request.correta())
            .pergunta(question)
            .build();

        alternative = alternativeRepository.save(alternative);
        return AlternativeResponse.fromEntity(alternative);
    }

    public void removeAlternative(Long id) {
        Alternative alternative = alternativeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alternativa não encontrada"));
        alternativeRepository.delete(alternative);
    }

    @Transactional
    public QuizAttemptResponse responder(Long quizId, QuizAnswerRequest request, User usuario) {
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new ResourceNotFoundException("Quiz não encontrado"));

        if (!quiz.isPublicado()) {
            throw new BusinessException("Quiz ainda não foi publicado");
        }

        QuizAttempt attempt = QuizAttempt.builder()
            .usuario(usuario)
            .quiz(quiz)
            .build();

        int pontuacao = 0;

        for (QuizAnswerRequest.Resposta resposta : request.respostas()) {
            Question pergunta = questionRepository.findById(resposta.perguntaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pergunta não encontrada"));

            Alternative alternativa = alternativeRepository.findById(resposta.alternativaId())
                .orElseThrow(() -> new ResourceNotFoundException("Alternativa não encontrada"));

            if (alternativa.isCorreta()) {
                pontuacao += pergunta.getPontuacao();
            }

            UserAnswer userAnswer = UserAnswer.builder()
                .tentativa(attempt)
                .pergunta(pergunta)
                .alternativa(alternativa)
                .build();

            attempt.getUserAnswers().add(userAnswer);
        }

        attempt.setPontuacao(pontuacao);
        attempt = quizAttemptRepository.save(attempt);

        return QuizAttemptResponse.fromEntity(attempt);
    }

    public List<QuizAttemptResponse> getRanking(Long quizId) {
        return quizAttemptRepository.findByQuizIdOrderByPontuacaoDesc(quizId)
            .stream().map(QuizAttemptResponse::fromEntity).toList();
    }
}
