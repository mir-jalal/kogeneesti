package net.mirjalal.kogeneesti.model;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.mirjalal.kogeneesti.enums.MediaType;
import net.mirjalal.kogeneesti.model.decorator.ImageUpload;

@Data
@Entity
@Table(name = "answers")
public class Answer implements ImageUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private MediaType type;
    private String answer;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Question question;
    private boolean correct;

    @Override
    public void setFileName(String name) {
        if(isImage()) {
            this.answer = name;
        }
    }

    @Override
    public Boolean isImage() {
        return MediaType.IMAGE.equals(type);
    }
}
