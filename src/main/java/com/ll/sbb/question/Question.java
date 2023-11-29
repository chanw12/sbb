package com.ll.sbb.question;

import com.ll.sbb.answer.Answer;
import com.ll.sbb.global.BaseEntity;
import com.ll.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Question extends BaseEntity {


    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "question",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;
    @ManyToMany
    Set<SiteUser> voter;


}
