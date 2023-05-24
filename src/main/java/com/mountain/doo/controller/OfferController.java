package com.mountain.doo.controller;

import com.mountain.doo.dto.AccountResponseDTO;
import com.mountain.doo.dto.OfferWriteRequestDTO;
import com.mountain.doo.entity.Offer;
import com.mountain.doo.entity.OfferImage;
import com.mountain.doo.service.OfferService;
import com.mountain.doo.util.FileUtil;
import com.mountain.doo.util.LoginUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/offer")
@Slf4j
public class OfferController {

    @Value("${file.upload.root-path}")
    private String rootPath;
    private final OfferService offerService;

    @GetMapping("/offer-main")
    public String offer() {
            log.info("추천메뉴");
            return "offer/offer-main";
    }



    @GetMapping("offer-eat")
    public String offerEat() {
        log.info("추천맛집");
        return "offer/offer-restaurant";
    }

    //글쓰기 버튼 클릭
    @GetMapping("write")
    public String write(){
        log.info("추천정보 글쓰기");

        return "offer/offer-writer";
    }

    @PostMapping("/offer-write")
    public String postWrite(OfferWriteRequestDTO dto, HttpSession session) {
        log.info("글쓰기 완료 요청");
        log.info("OfferWriteRequestDTO : "+dto);
//        log.info("imageDto" + imgDto);

        List<String> filePathList = new ArrayList<>();
        dto.getOfferImage().forEach(img -> {
            if (!img.isEmpty()) {
                log.info("img-name: {}", img.getOriginalFilename());
                String filePath = FileUtil.uploadFile(img, rootPath);
                log.info("file-patj: {}", filePath);
                filePathList.add(filePath);
            }
        });
        AccountResponseDTO attribute = (AccountResponseDTO) session.getAttribute(LoginUtil.LOGIN_KEY);
        offerService.save(dto,attribute.getAccountId(), filePathList);

//        return "redirect:/offer/offer-main";
        return "";
    }

}
