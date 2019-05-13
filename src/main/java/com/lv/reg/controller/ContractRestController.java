package com.lv.reg.controller;

import com.lv.reg.service.ContractService;
import com.lv.reg.service.FilesStoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping(path = "/ajax/contract")
@RestController()
public class ContractRestController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private FilesStoringService filesStoringService;


    @RequestMapping(path = "/{id}/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteContract(@PathVariable("id") long id){
        contractService.deleteContract(id);
        return "Deleted";
    }

    @GetMapping("{id}/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable long id, @PathVariable String filename) {

        Resource file = filesStoringService.loadAsResource(contractService.findById(id), filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(path = "/{id}/close", method = RequestMethod.PUT)
    @ResponseBody
    public String closeContract(@PathVariable("id") long id) {
        contractService.closeContract(id);
        return "Closed";
    }
}
