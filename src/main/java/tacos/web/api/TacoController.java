package tacos.web.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/tacos", produces = "application/json")
@CrossOrigin(origins = "http://tacocloud:8080")
public class TacoController {

    private TacoRepository tacoRepo;

    public TacoController(TacoRepository tacoReposytory) {
        tacoRepo = tacoReposytory;
    }

    @GetMapping(params = "recent")
    public Iterable<Taco> recentTaco() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepo.findAll(page).getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
        Optional<Taco> optTaco = tacoRepo.findById(id);
        return optTaco.map(taco -> new ResponseEntity<>(taco, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) {
        return tacoRepo.save(taco);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Taco putTaco(@Param("id") Long id, @RequestBody Taco taco) {
        taco.setId(id);
        return tacoRepo.save(taco);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public Taco patchTaco(@Param("id") Long id, @RequestBody Taco tacoPatch) {
        Taco tacoDB = tacoRepo.findById(id).get();
        if (tacoPatch.getName() != null) {
            tacoDB.setName(tacoPatch.getName());
        }
        if (tacoPatch.getIngredients() != null) {
            tacoDB.setIngredients(tacoPatch.getIngredients());
        }
        if (tacoPatch.getCreatedAt() != null) {
            tacoDB.setCreatedAt(tacoPatch.getCreatedAt());
        }
        return tacoRepo.save(tacoDB);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@Param("id") Long id) {
        tacoRepo.deleteById(id);
    }
}
