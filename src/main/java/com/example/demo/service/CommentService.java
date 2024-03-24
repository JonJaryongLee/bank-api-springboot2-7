package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글의 전체 댓글을 가져옵니다.
     *
     * @param articleNo 게시글 아이디
     * @return 게시글의 댓글 목록
     * @throws NoSuchElementException articleNo 에 해당하는 게시글이 없다면 예외 발생
     */
    public List<Comment> findComments(Long articleNo) {
        Article article = verifyArticleExists(articleNo);
        return commentRepository.findByArticle(article);
    }

    /**
     * 주어진 번호에 해당하는 댓글을 조회합니다.
     *
     * @param commentNo 조회할 댓글 번호
     * @return 조회된 댓글 객체
     * @throws NoSuchElementException 만약 존재하지 않는 댓글이라면, NoSuchElementException 을 발생시킵니다.
     */
    public Comment findComment(Long commentNo) {
        Comment comment = verifyCommentExists(commentNo);
        return comment;
    }

    private Comment verifyCommentExists(Long commentNo) {
        return commentRepository.findById(commentNo).orElseThrow(
                () -> new NoSuchElementException("Comment does not exist"));
    }

    /**
     * 새로운 댓글을 생성합니다.
     *
     * @param username
     * @param articleNo
     * @param content
     * @return 생성한 댓글의 아이디
     * @throws IllegalArgumentException username, articleNo, content 가 비어있다면 IllegalArgumentException을 발생시킵니다.
     */
    @Transactional
    public Long createComment(String username, Long articleNo, String content) {
        verifyEmptyFields(username, articleNo, content);
        Member foundMember = validateMember(username);
        Article foundArticle = verifyArticleExists(articleNo);
        Comment comment = new Comment();
        comment.setMember(foundMember);
        comment.setArticle(foundArticle);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }

    /**
     * 주어진 번호에 해당하는 댓글을 삭제합니다.
     *
     * @param commentNo 삭제할 댓글 번호
     * @return 삭제한 댓글의 아이디
     */
    @Transactional
    public Long deleteComment(Long commentNo) {
        commentRepository.deleteById(commentNo);
        return commentNo;
    }

    /**
     * 댓글을 수정합니다.
     *
     * @param commentNo
     * @param username
     * @param articleNo
     * @param content
     * @return 수정한 댓글의 아이디
     * @throws IllegalArgumentException commentNo, username, articleNo, content 가 비어있다면 예외를 발생시킵니다.
     * @throws NoSuchElementException   commentNo, username, articleNo 에 해당하는 객체가 존재하지 않는다면 예외를 발생시킵니다.
     */
    @Transactional
    public Long updateComment(Long commentNo, String username, Long articleNo, String content) {
        verifyEmptyCommentNo(commentNo);
        verifyEmptyFields(username, articleNo, content);
        verifyCommentExists(commentNo);
        Member foundMember = validateMember(username);
        Article foundArticle = verifyArticleExists(articleNo);

        Comment comment = new Comment();
        comment.setId(commentNo);
        comment.setArticle(foundArticle);
        comment.setMember(foundMember);
        comment.setContent(content);
        commentRepository.findById(commentNo).ifPresent(c -> {
            comment.setCreatedAt(c.getCreatedAt());
        });
        comment.setUpdatedAt(LocalDateTime.now());
        Comment updatedComment = commentRepository.save(comment);
        return updatedComment.getId();
    }

    private void verifyEmptyCommentNo(Long commentNo) {
        if (commentNo == null) {
            throw new IllegalArgumentException("CommentNo cannot be empty");
        }
    }

    private void verifyEmptyFields(String username, Long articleNo, String content) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        } else if (articleNo == null) {
            throw new IllegalArgumentException("ArticleNo cannot be empty");
        } else if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
    }

    private Member validateMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Member does not exist"));
    }


    private Article verifyArticleExists(Long articleNo) {
        return articleRepository.findById(articleNo).orElseThrow(
                () -> new NoSuchElementException("Article does not exist"));
    }
}
