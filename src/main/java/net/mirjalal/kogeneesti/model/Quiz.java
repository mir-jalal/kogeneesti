package net.mirjalal.kogeneesti.model;

import java.math.BigInteger;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import net.mirjalal.kogeneesti.exception.NotFoundException;

@Data
@Entity
@Table(name = "quizzes")
public class Quiz {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;
    @OneToMany(
        mappedBy = "quiz", 
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private List<Question> questions;
    public Question getQuestionById(BigInteger questionId) {
        return questions.stream()
                .filter(question -> question.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Question not found in the specified quiz"));
    }
    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setQuiz(null);
    }
}
