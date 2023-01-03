package com.dev.pleaseTakecareFiveDucks.anime.service;

import com.dev.pleaseTakecareFiveDucks.anime.domain.dto.request.*;
import com.dev.pleaseTakecareFiveDucks.anime.domain.vo.AnimeThumbnailVO;
import com.dev.pleaseTakecareFiveDucks.anime.domain.vo.AnimeVO;
import com.dev.pleaseTakecareFiveDucks.config.db.mapper.AnimeDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimeServiceImpl implements AnimeService{

    private final AnimeDAO animeDAO;

    private boolean validateFileAttachedOrNot(String filePath, String fileName) {
        if(!filePath.isEmpty() && !fileName.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer selectAnimeTotalCnt() {
        return animeDAO.getAnimeTotalCnt();
    }

    @Override
    public void removeAllAnimeInfoList() throws Exception {

        // 만약 anime이 0개 초과로 있다면...
        int animeCnt = animeDAO.getAnimeTotalCnt();

        if(animeCnt > 0) {

            // 실제 삭제가 된 데이터들이 size와 일치해야 합니다.
            int removedAnimeCnt = animeDAO.deleteAll();

            // 만약, 일치하지 않는다면 Exception을 throw 합니다.
            if(animeCnt != removedAnimeCnt) {
                throw new Exception();
            }
        }
    }

    // todo: 추후 jsp에서 어떻게 받을지 결정한 후에 개발하는 것으로 할게요.
    @Override
    public List<AnimeVO> selectAnimeList(SelectAnimePaginationRequestDTO selectAnimePaginationRequestDTO) {
        return null;
    }

    @Override
    public List<AnimeVO> selectAllAnimeInfoList() {

        List<AnimeVO> animeVOList = animeDAO.selectAllAnimeList();

        List<Integer> animeNoList = animeVOList
                .stream()
                .map(AnimeVO::getAnimeNo)
                .collect(Collectors.toList());

        SelectAnimeThumbnailImageListRequestDTO selectAnimeThumbnailImageListRequestDTO = SelectAnimeThumbnailImageListRequestDTO.builder()
                .animeNoList(animeNoList)
                .build();

        List<AnimeThumbnailVO> animeThumbnailVOList = animeDAO.selectAnimeThumbnailImageListByAnimeNo(selectAnimeThumbnailImageListRequestDTO);

        // animeVOList를 순회하며, animeNo가 같은 요소들끼리 찾아서 filePullPath를 set해줍니다.
        animeVOList.forEach(e -> {

                    //
                    animeThumbnailVOList.forEach(f -> {
                                if(e.getAnimeNo().equals(f.getAnimeNo())) {
                                    e.setFilePullPath(f.getFilePullPath());
                                }
                    });
        });

        return animeVOList;
    }

    @Override
    public AnimeVO selectAnimeInfo(SelectAnimeInfoRequestDTO selectAnimeInfoRequestDTO) {

        // 단일 조회 시, left outer join 으로 조회수와 썸네일을 조회합니다.
        // 리스트 같은경우 다르게 조회하므로 selectAllAnimeInfoList를 참고해주시면 됩니다.

        return animeDAO.selectAnimeInfo(selectAnimeInfoRequestDTO);
    }

    @Override
    public void registerAnimeInfo(InsertAnimeInfoRequestDTO insertAnimeInfoRequestDTO) throws Exception {

        // 단일 insert 이므로, 한개가 insert되지 않으면 exception을 일으킵니다.
        if(animeDAO.insertAnimeInfo(insertAnimeInfoRequestDTO) != 1) {
            throw new Exception();
        }

        // 만약 썸네일을 첨부했다면...
        String filePath = insertAnimeInfoRequestDTO.getFilePath();
        String fileName = insertAnimeInfoRequestDTO.getFileName();

        if(validateFileAttachedOrNot(filePath, fileName)) {

            // 썸네일을 삽입하기 위한 dto를 준비합니다.

            InsertAnimeThumbnailInfoRequestDTO insertAnimeThumbnailInfoRequestDTO = InsertAnimeThumbnailInfoRequestDTO.builder()
                    .animeNo(insertAnimeInfoRequestDTO.getInsertedAnimeNo())
                    .filePath(filePath)
                    .fileName(fileName)
                    .build();

            // 썸네일을 삽입합니다.
            if(animeDAO.insertAnimeThumbnailInfo(insertAnimeThumbnailInfoRequestDTO) != 1) {
                throw new Exception();
            }
        }
    }

    @Override
    public void modifyAnimeInfo(UpdateAnimeInfoRequestDTO updateAnimeInfoRequestDTO) throws Exception {

        // 단일 update 이므로, 한개가 insert되지 않으면 exception을 일으킵니다.
        if(animeDAO.updateAnimeInfo(updateAnimeInfoRequestDTO) != 1) {
           throw new Exception();
       }

        // 만약 썸네일을 첨부했다면...
        String filePath = updateAnimeInfoRequestDTO.getFilePath();
        String fileName = updateAnimeInfoRequestDTO.getFileName();

        if(validateFileAttachedOrNot(filePath, fileName)) {

            // 썸네일을 update하기 위한 dto를 준비합니다.

            UpdateAnimeThumbnailInfoRequestDTO updateAnimeThumbnailInfoRequestDTO = UpdateAnimeThumbnailInfoRequestDTO.builder()
                    .animeNo(updateAnimeInfoRequestDTO.getAnimeNo())
                    .filePath(filePath)
                    .fileName(fileName)
                    .build();

            // 썸네일을 삽입합니다.
            if(animeDAO.updateAnimeThumbnailInfo(updateAnimeThumbnailInfoRequestDTO) != 1) {
                throw new Exception();
            }
        }
    }

    @Override
    public void removeAnimeInfo(RemoveAnimeInfoRequestDTO removeAnimeRequestDTO) throws Exception {

        if(animeDAO.deleteAnimeInfo(removeAnimeRequestDTO.getAnimeNo()) != 1) {
            throw new Exception();
        }

        if(animeDAO.deleteAnimeThumbnailInfo(removeAnimeRequestDTO.getAnimeNo()) != 1) {
            throw new Exception();
        }
    }
}
