package nl.wtrlmn.skm;

import nl.wtrlmn.skm.controllers.TournamentController;
import nl.wtrlmn.skm.filter.JwtRequestFilter;
import nl.wtrlmn.skm.services.TournamentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(
        controllers = TournamentController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtRequestFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TournamentService tournamentService;

    @Test
    public void shouldReturnOkForTournaments() throws Exception {
        this.mockMvc.perform(get("/api/tournaments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}