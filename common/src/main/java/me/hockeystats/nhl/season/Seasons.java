package me.hockeystats.nhl.season;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;
import java.util.List;
import me.hockeystats.BaseRepository;
import reactor.core.publisher.Mono;

public class Seasons extends BaseRepository<Season> {
  public Seasons(EntityManager entityManager) {
    super(entityManager);
  }

  public Mono<Season> findById(long id) {
    return Mono.fromCallable(
        () -> {
          EntityQueryRequest request =
              entityManager.createEntityQueryRequest(
                  "SELECT * FROM Season WHERE seasonId = @seasonId");
          request.setNamedBinding("seasonId", id);
          QueryResponse<Season> response =
              entityManager.executeEntityQueryRequest(Season.class, request);
          List<Season> seasons = response.getResults();
          if (seasons.size() > 1) {
            throw new IllegalStateException("Multiple entries for the same season");
          } else if (seasons.size() == 0) {
            return null;
          }
          return seasons.get(0);
        });
  }
}
