package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    CommentService commentService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    @Test
    public void findCommentsTest() {
        // given
        Member member = new Member();
        member.setUsername("testusername");
        member.setPassword("testpassword");
        member.setNickname("testnickname");
        memberRepository.save(member);

        Article article = new Article();
        article.setMember(member);
        article.setTitle("testtitle");
        article.setContent("testcontent");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);

        Comment comment1 = new Comment();
        comment1.setArticle(article);
        comment1.setMember(member);
        comment1.setContent("testcontent1");
        comment1.setCreatedAt(LocalDateTime.now());
        comment1.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setArticle(article);
        comment2.setMember(member);
        comment2.setContent("testcontent2");
        comment2.setCreatedAt(LocalDateTime.now());
        comment2.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment2);

        // when
        List<Comment> comments = commentService.findComments(article.getId());

        // then
        Assertions.assertThat(comments.size()).isEqualTo(2);
    }

    @Test
    public void findCommentTest() {
        // given
        Member member = new Member();
        member.setUsername("username");
        member.setPassword("password");
        member.setNickname("nickname");
        memberRepository.save(member);

        Article article = new Article();
        article.setMember(member);
        article.setTitle("testtitle");
        article.setContent("testcontent");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setArticle(article);
        comment.setContent("testcontent");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // when
        Comment foundComment = commentService.findComment(comment.getId());

        // then
        Assertions.assertThat(foundComment.getId()).isEqualTo(comment.getId());
        Assertions.assertThat(foundComment.getMember().getUsername()).isEqualTo("username");
        Assertions.assertThat(foundComment.getArticle().getTitle()).isEqualTo("testtitle");
        Assertions.assertThat(foundComment.getContent()).isEqualTo("testcontent");
//         Assertions.assertThat(foundComment.getCreatedAt()).isEqualTo(LocalDateTime.now());
//         Assertions.assertThat(foundComment.getUpdatedAt()).isEqualTo(LocalDateTime.now());
    }

    @Test
    public void deleteCommentTest() {
        // given
        Member member = new Member();
        member.setUsername("username");
        member.setPassword("password");
        member.setNickname("nickname");
        memberRepository.save(member);

        Article article = new Article();
        article.setMember(member);
        article.setTitle("testtitle");
        article.setContent("testcontent");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setArticle(article);
        comment.setContent("testcontent");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // when
        Long deletedId = commentService.deleteComment(comment.getId());
        Optional<Comment> invalidId = commentRepository.findById(deletedId);

        // then
        Assertions.assertThat(deletedId).isEqualTo(comment.getId());
        Assertions.assertThat(invalidId).isEqualTo(Optional.empty());
    }

    @Test
    public void updateCommentTest() {
        // given
        Member member = new Member();
        member.setUsername("username");
        member.setPassword("password");
        member.setNickname("nickname");
        memberRepository.save(member);

        Article article = new Article();
        article.setMember(member);
        article.setTitle("testtitle");
        article.setContent("testcontent");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setArticle(article);
        comment.setContent("testcontent");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // when
        Long updatedCommentId = commentService.updateComment(comment.getId(), member.getUsername(), article.getId(), "fixedcontent");
        Comment foundComment = commentRepository.findById(updatedCommentId).get();

        // then
        Assertions.assertThat(foundComment.getId()).isEqualTo(updatedCommentId);
        Assertions.assertThat(foundComment.getMember().getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(foundComment.getArticle().getTitle()).isEqualTo(article.getTitle());
        Assertions.assertThat(foundComment.getContent()).isEqualTo("fixedcontent");
//        Assertions.assertThat(foundComment.getCreatedAt()).isEqualTo(foundComment.getUpdatedAt());
    }

    @Test
    public void validateMemberTest() {
        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> commentService.createComment("invaliduser", 1L, "content"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Member does not exist");

    }

    @Test
    public void verifyArticleExistTest() {
        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> commentService.findComments(99L));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Article does not exist");
    }

    @Test
    public void verifyCommentExistTest() {
        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> commentService.findComment(99L));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Comment does not exist");
    }

    @Test
    public void verifyEmptyCommentNoTest() {
        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> commentService.updateComment(null, "testusername", 22L, "testcontent"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("CommentNo cannot be empty");
    }

    @Test
    public void verifyEmptyUsernameTest() {
        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> commentService.updateComment(22L, "    ", 22L, "testcontent"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Username cannot be empty");
    }

    @Test
    public void verifyEmptyArticleNoTest() {
        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> commentService.updateComment(22L, "testusername", null, "testcontent"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("ArticleNo cannot be empty");
    }

    @Test
    public void verifyEmptyContentTest() {
        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> commentService.updateComment(22L, "testusername", 22L, "    "));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Content cannot be empty");
    }
}