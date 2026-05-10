package net.mirjalal.kogeneesti.model;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private BigInteger id;
    private String word;
    private String translation;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "dictionary_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Dictionary dictionary;
    
    public BigInteger getDictionaryId() {
        return dictionary.getId();
    }
}
