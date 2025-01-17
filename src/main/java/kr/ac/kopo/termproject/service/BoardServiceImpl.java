package kr.ac.kopo.termproject.service;

import kr.ac.kopo.termproject.dto.BoardDTO;
import kr.ac.kopo.termproject.dto.PageRequestDTO;
import kr.ac.kopo.termproject.dto.PageResultDTO;
import kr.ac.kopo.termproject.entity.Borad;
import kr.ac.kopo.termproject.entity.Member;
import kr.ac.kopo.termproject.repository.BoardRepository;
import kr.ac.kopo.termproject.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository repository;
    private final ReplyRepository replyRepository;
    @Override
    public Long register(BoardDTO dto) {
        Borad board = dtoToEntity(dto);
        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Borad) en[0], (Member) en[1],(Long) en[2]));
//        Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
        Page<Object[]> result = repository.searchPage(pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;
        BoardDTO boardDTO = entityToDTO((Borad) arr[0], (Member) arr[1], (Long) arr[2]);

        return boardDTO;
    }

    @Transactional
    @Override
    public void removewithReplies(Long bno) {
        // 댓글삭제
        replyRepository.deleteByBno(bno);
        // 원글삭제
        repository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {
        Borad board = repository.getReferenceById(boardDTO.getBno());
        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        repository.save(board);
    }


}