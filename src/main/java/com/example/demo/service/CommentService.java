package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 모든 댓글을 조회합니다.
     * @return 댓글 목록
     */
    public List<Comment> findComments() {
        return commentRepository.findAll();
    }

    /**
     * 모든 댓글을 조회하되, 작성자와 게시글 정보를 포함합니다.
     * @return 댓글 목록
     */
    public List<Comment> findCommentsWithUserAndArticle() {
        return commentRepository.findAllWithUserAndArticle();
    }

    /**
     * 주어진 번호에 해당하는 댓글을 조회합니다.
     * @param commentNo 조회할 댓글 번호
     * @return 조회된 댓글
     */
    public Comment findComment(Long commentNo) {
        Comment comment = verifyCommentExists(commentNo);
        return comment;
    }

    /**
     * 주어진 번호에 해당하는 댓글을 조회하되, 작성자와 게시글 정보를 포함합니다.
     * @param commentNo 조회할 댓글 번호
     * @return 조회된 댓글
     */
    public Comment findCommentWithUserAndArticle(Long commentNo) {
        verifyCommentExists(commentNo);
        return commentRepository.findByIdWithUserAndArticle(commentNo)
                .orElse(new Comment());
    }

    /**
     * 새로운 댓글을 생성합니다.
     * @param comment 생성할 댓글
     */
    @Transactional
    public void createComment(Comment comment) {
        verifyCommentNotNull(comment);
        verifyEmptyFields(comment);
        commentRepository.save(comment);
    }

    /**
     * 주어진 번호에 해당하는 댓글을 삭제합니다.
     * @param commentNo 삭제할 댓글 번호
     */
    @Transactional
    public void deleteComment(Long commentNo) {
        Comment comment = findComment(commentNo);
        commentRepository.deleteById(commentNo);
    }

    /**
     * 댓글을 업데이트합니다.
     * @param comment 업데이트할 댓글
     */
    @Transactional
    public void updateComment(Comment comment) {
        verifyCommentNotNull(comment);
        verifyEmptyFields(comment);
        verifyCommentExists(comment.getId());
        commentRepository.save(comment);
    }

    /**
     * 주어진 댓글이 존재하는지 검증합니다.
     * @param commentNo 검증할 댓글 번호
     * @throws IllegalStateException 만약 존재하지 않는 댓글이라면, IllegalStateException 을 발생시킵니다.
     */
    private Comment verifyCommentExists(Long commentNo) {
        return commentRepository.findById(commentNo).orElseThrow(
                () -> new IllegalStateException("Comment does not exist"));
    }

    /**
     * 주어진 댓글이 null이 아닌지 검증합니다.
     * @param comment 검증할 댓글
     * @throws IllegalArgumentException 만약 전달한 comment 가 null 이라면 IllegalArgumentException 을 발생시킵니다.
     */
    private void verifyCommentNotNull(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment is null");
        }
    }

    /**
     * 주어진 댓글의 필드가 비어있는지 검증합니다.
     * @param comment 검증할 댓글
     */
    private void verifyEmptyFields(Comment comment) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
    }
}
