package org.test.sotfgen.service.classes;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.audit.PostHistoryEntity;
import org.test.sotfgen.dto.PostHistorySearchParams;
import org.test.sotfgen.repository.PostHistoryRepository;
import org.test.sotfgen.service.interfaces.PostHistoryService;

@Service
@RequiredArgsConstructor
public class PostHistoryServiceImpl implements PostHistoryService {

    private final PostHistoryRepository postHistoryRepository;

    @Override
    public Page<PostHistoryEntity> getAllPostHistory(PostHistorySearchParams params, Pageable pageable) {
        return postHistoryRepository.findAll((root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (params.postId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("post_id"), params.postId()));
            }
            if (params.title() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + params.title() + "%"));
            }
            if (params.body() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("body"), "%" + params.body() + "%"));
            }
            if (params.ownerId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("owner_id"), params.ownerId()));
            }
            if (params.groupId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("group_id"), params.groupId()));
            }
            if (params.hidden() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("hidden"), params.hidden()));
            }
            return predicate;
        }, pageable);
    }
}
