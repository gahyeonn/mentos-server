package MentosServer.mentos.repository;

import MentosServer.mentos.model.domain.Member;
import MentosServer.mentos.model.domain.Mentee;
import MentosServer.mentos.model.domain.Mento;
import MentosServer.mentos.model.dto.PostMentosMajorReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class SettingRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //학교 전공, 학번 수정
    public void changeSchoolInfo(int memberId, String major, int schoolId){
        String query = "update member set memberMajor = ?, memberStudentId=? where memberId=?";
        Object[] params = new Object[]{major,schoolId,memberId};
        this.jdbcTemplate.update(query,params);
    }

    //닉네임 변경
    public void changeNickName(int memberId, String nickName) {
        String query = "update member set memberNickName=? where memberId=?";
        Object[] params = new Object[]{nickName,memberId};
        this.jdbcTemplate.update(query,params);
    }

    //프로필 자기소개 변경
    public void changeMenteeProfileIntro(int memberId, String intro) {
        String query = "update menti set mentiIntro=? where memberId=?";
        Object[] params = new Object[]{intro,memberId};
        this.jdbcTemplate.update(query,params);
    }

    public void changeMentoProfileIntro(int memberId, String intro) {
        String query = "update mento set mentoIntro=? where memberId=?";
        Object[] params = new Object[]{intro,memberId};
        this.jdbcTemplate.update(query,params);
    }

    //이전 프로필 이미지 가져오기
    public Optional<String> getMentoProfileImageUrl(int memberId) {
        String query = "select mentoImage from mento where memberId=?";
        return Optional.ofNullable(this.jdbcTemplate.queryForObject(query,String.class,memberId));
    }
    public Optional<String> getMenteeProfileImageUrl(int memberId) {
        String query = "select mentiImage from menti where memberId=?";
        return Optional.ofNullable(this.jdbcTemplate.queryForObject(query,String.class,memberId));
    }

    //프로필 이미지 삭제
    public void deleteMentoProfileImage(int memberId) {
        String query = "update mento set mentoImage=null where memberId=?";
        this.jdbcTemplate.update(query,memberId);
    }
    public void deleteMenteeProfileImage(int memberId) {
        String query = "update menti set mentiImage=null where memberId=?";
        this.jdbcTemplate.update(query,memberId);
    }

    //프로필 이미지 변경
    public void updateMentoProfileImage(int memberId, String newImageUrl) {
        String query = "update mento set mentoImage=? where memberId=?";
        Object[] params = new Object[]{newImageUrl,memberId};
        this.jdbcTemplate.update(query,params);
    }
    public void updateMenteeProfileImage(int memberId, String newImageUrl) {
        String query = "update menti set mentiImage=? where memberId=?";
        Object[] params = new Object[]{newImageUrl,memberId};
        this.jdbcTemplate.update(query,params);
    }

    //멘토스 계열 수정
    public void changeMentoMentosMajor(int memberId, PostMentosMajorReq mentosMajorReq) {
        String query = "update mento set mentoMajorFirst=?, mentoMajorSecond=? where memberId=?";
        Object[] params = new Object[]{mentosMajorReq.getMentosMajorFirst(),mentosMajorReq.getMentosMajorSecond(),memberId};
        this.jdbcTemplate.update(query,params);
    }
    public void changeMenteeMentosMajor(int memberId, PostMentosMajorReq mentosMajorReq) {
        String query = "update menti set mentiMajorFirst=?, mentiMajorSecond=? where memberId=?";
        Object[] params = new Object[]{mentosMajorReq.getMentosMajorFirst(),mentosMajorReq.getMentosMajorSecond(),memberId};
        this.jdbcTemplate.update(query,params);
    }

    public Mento getMentoProfile(int memberId) {
        String query = "select * from member natural join mento where memberId=?";
        Mento mento = this.jdbcTemplate.queryForObject(query,
                (rs,rowNum) -> (Mento.builder()
                        .member(new Member(
                                rs.getInt("memberId"),
                                rs.getString("memberName"),
                                rs.getString("memberNickName"),
                                rs.getInt("memberStudentId"),
                                rs.getString("memberEmail"),
                                rs.getString("memberPw"),
                                rs.getInt("memberStudentId"),
                                rs.getString("memberMajor"),
                                rs.getString("memberSex"),
                                rs.getInt("memberMentos"),
                                rs.getString("memberStatus"),
                                rs.getTimestamp("memberCreateAt"),
                                rs.getTimestamp("memberUpdateAt")
                        ))
                        .mentoMajorFirst(rs.getInt("mentoMajorFirst"))
                        .mentoMajorSecond(rs.getInt("mentoMajorSecond"))
                        .mentoImage(rs.getString("mentoImage"))
                        .mentoIntro(rs.getString("mentoIntro"))
                        .build())
                ,memberId
                );
        return mento;
    }

    public Mentee getMenteeProfile(int memberId) {
        String query = "select * from member natural join menti where memberId=?";
        Mentee mentee = this.jdbcTemplate.queryForObject(query,
                (rs,rowNum) -> Mentee.builder()
                        .member(new Member(
                                rs.getInt("memberId"),
                                rs.getString("memberName"),
                                rs.getString("memberNickName"),
                                rs.getInt("memberStudentId"),
                                rs.getString("memberEmail"),
                                rs.getString("memberPw"),
                                rs.getInt("memberStudentId"),
                                rs.getString("memberMajor"),
                                rs.getString("memberSex"),
                                rs.getInt("membermentees"),
                                rs.getString("memberStatus"),
                                rs.getTimestamp("memberCreateAt"),
                                rs.getTimestamp("memberUpdateAt")
                        ))
                        .menteeMajorFirst(rs.getInt("mentiMajorFirst"))
                        .menteeMajorSecond(rs.getInt("mentiMajorSecond"))
                        .menteeImage(rs.getString("mentiImage"))
                        .menteeIntro(rs.getString("mentiIntro"))
                        .build()
                ,memberId
        );
        return mentee;
    }
}
