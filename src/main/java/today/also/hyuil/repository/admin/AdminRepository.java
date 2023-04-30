package today.also.hyuil.repository.admin;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.member.Admin;
import today.also.hyuil.domain.member.QAdmin;

import javax.persistence.EntityManager;

import static today.also.hyuil.domain.member.QAdmin.admin;

@Transactional
@Repository
public class AdminRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public AdminRepository(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    public Admin findByAdminId(String adminId) {
        return query.select(admin)
                .from(admin)
                .where(admin.adminId.eq(adminId))
                .stream().findFirst()
                .orElse(null);
    }
}
