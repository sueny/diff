package com.ssm.diff;

import com.ssm.diff.domain.DiffData;
import com.ssm.diff.domain.LeftData;
import com.ssm.diff.domain.RightData;
import com.ssm.diff.services.DiffDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DiffDataController {

    private DiffDataService diffDataService;

    @Autowired
    public DiffDataController(DiffDataService diffDataService) {
        this.diffDataService = diffDataService;
    }

    @PostMapping("/v1/diff/{id}/left")
    public ResponseEntity addLeftData(@PathVariable Long id, @Valid @RequestBody LeftData leftData) {
        leftData = diffDataService.saveLeftData(id, leftData);
        return ResponseEntity.status(HttpStatus.CREATED).body(leftData);
    }

    @PostMapping("/v1/diff/{id}/right")
    public ResponseEntity addRightData(@PathVariable Long id, @Valid @RequestBody RightData rightData) {
        rightData = diffDataService.saveRightData(id, rightData);
        return ResponseEntity.status(HttpStatus.CREATED).body(rightData);
    }

    @GetMapping("/v1/diff/{id}")
    public ResponseEntity getDiff(@PathVariable Long id) {
        DiffData diff = diffDataService.getDiff(id);
        return ResponseEntity.ok(diff);
    }
}
