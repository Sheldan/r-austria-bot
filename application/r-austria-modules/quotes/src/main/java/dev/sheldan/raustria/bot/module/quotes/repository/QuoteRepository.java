package dev.sheldan.raustria.bot.module.quotes.repository;

import dev.sheldan.abstracto.core.models.ServerSpecificId;
import dev.sheldan.abstracto.core.models.database.AServer;
import dev.sheldan.abstracto.core.models.database.AUserInAServer;
import dev.sheldan.raustria.bot.module.quotes.model.database.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, ServerSpecificId> {
    List<Quote> findByTextContainingAndServer(String text, AServer server);
    List<Quote> findByServer(AServer server);
    List<Quote> findByAuthor(AUserInAServer author);
    Long countByAuthor(AUserInAServer author);
    Long countByAdder(AUserInAServer adder);
}
