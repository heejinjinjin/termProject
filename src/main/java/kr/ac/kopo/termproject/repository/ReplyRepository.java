package kr.ac.kopo.termproject.repository;

import kr.ac.kopo.termproject.entity.Borad;
import kr.ac.kopo.termproject.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 게시글 삭제시에 댓글 삭제
    @Modifying
    @Query("delete from Reply r where r.board.bno = :bno")
    void deleteByBno(Long bno);

    // 게시글 번호에 해당하는 댓글 목록 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Borad board);

}
