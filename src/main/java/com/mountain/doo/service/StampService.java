package com.mountain.doo.service;

import com.mountain.doo.dto.stamp.StampAddConditionDTO;
import com.mountain.doo.dto.stamp.StampResponseDTO;
import com.mountain.doo.entity.Stamp;
import com.mountain.doo.repository.StampMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@Slf4j
@RequiredArgsConstructor
public class StampService {
    private final StampMapper mapper;

    public Stamp stampCount(String accountId) {

        mapper.myBoard(accountId);

        // 업데이트 된 정보 전달
        Stamp stampCount = mapper.stampCount(accountId);
        StampAddConditionDTO dto = new StampAddConditionDTO();
        dto.setAccountId(accountId);
        update(dto);
        return stampCount;

    }

    public StampResponseDTO stampResponseDTO(String accountId){
        Stamp stamp = mapper.stampCount(accountId);

        StampResponseDTO dto = new StampResponseDTO(stamp);
        System.out.println("dto = " + dto);
        return dto;
    }
    public void clickEvent(StampAddConditionDTO dto) {
        if(dto.isClickEvent()==true) {
            System.out.println("dto2222 = " + dto);
            int stampTotal = mapper.totalCount(dto.getAccountId());
            System.out.println("stampTotal = " + stampTotal);
            // 스탬프 18개 이상일때만 동작
            if (stampTotal >= 18) {
                mapper.openEvent(dto.getAccountId());

            }
        }
    }

    public void update(StampAddConditionDTO dto) {
        log.info("stampService update : " + dto.getAccountId());
        //조건별 데이터 전달
        stampAddCondition(dto);

        //응모권 쓰면 -18개
        if (dto.isClickEvent()) {
            clickEvent(dto);
        }

        // 업데이트 된 정보 전달
        Stamp stampCount = mapper.stampCount(dto.getAccountId());
        log.info("StampService mapper : " + stampCount);
//        return stampCount;

    }


    //로그인, 게시글작성, 배너 클릭시 각각 칼럼 count ++
    //스탬프 +1 되는 조건 충족시 도장개수 +1
    public void stampAddCondition(StampAddConditionDTO dto) {
        boardBanner(dto);
        plusStamp(dto);

    }

    //여기에 변수 하나 만들어서 로그인 true 면 조건 넣으시고
    public void boardBanner(StampAddConditionDTO dto) {
        boolean ac = dto.isBannerClickCount();

//    boolean attendCount = dto.isAttendCount();


        if (ac) mapper.bannerPlus(dto.getAccountId());

//    mapper.isLogin(dto.isAttendCount(),dto.getAccountId());

    }


    public void plusStamp(StampAddConditionDTO dto) {
        int todayMyBoard = mapper.todayMyBoard(dto.getAccountId());

        System.out.println("todayMyBoard = " + todayMyBoard);


        Stamp stamp = mapper.stampCount(dto.getAccountId());
        System.out.println("!!!stamp = " + stamp);

        int checkBannerStamp = stamp.getCheckBannerStamp();
        int checkBoardStamp = stamp.getCheckBoardStamp();

        //오늘 쓴 게시물이 3개면 도장 찍기
        if(checkBoardStamp==0){
        if (stamp.getBoardCount() == 3)  {
            mapper.stampAdd(dto.getAccountId());
            mapper.checkBoardStamp(dto.getAccountId());
        }
        }
        if(checkBannerStamp==0) {
            if (stamp.getBannerClickCount() == 3) {
                mapper.stampAdd(dto.getAccountId());
                mapper.checkBannerStamp(dto.getAccountId());
            }
        }
    }






    public void stampAdd(String accountId) {
        log.info("stampAdd 진입" + accountId);
        mapper.updateClickStamp(true, accountId);
        mapper.currentAdd(accountId);
        mapper.stampAdd(accountId);
    }

    public void isLogin(boolean attendCount, String accountId) {
        mapper.isLogin(attendCount, accountId);
    }



    public boolean isClick(String accountId) {
        boolean click = mapper.isClick(accountId);
        return click;
    }




    //해당아이디로 오늘 쓴 게시글 수 count 확인

}



