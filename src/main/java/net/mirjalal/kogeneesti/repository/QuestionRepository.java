package net.mirjalal.kogeneesti.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;
import net.mirjalal.kogeneesti.model.Question;

@Transactional
public interface QuestionRepository extends JpaRepository<Question, BigInteger> {
    public Optional<Question> getByQuestion(String question);
}
