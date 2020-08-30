package cn.marwin.adminsystem.repository.platform;

import cn.marwin.adminsystem.entity.platform.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateDAO extends JpaRepository<Candidate, Integer> {
    List<Candidate> findByIid(Integer iid);
    List<Candidate> findByUid(Integer uid);
    Optional<Candidate> findByIidAndUid(Integer iid, Integer uid);
    Boolean existsByIidAndUid(Integer iid, Integer uid);
    Integer countByIid(Integer iid);
}
