package com.example.apijsondemo;

import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.framework.APIJSONController;
import apijson.orm.Parser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Api extends APIJSONController<Long> {

    @Override
    public Parser<Long> newParser(HttpSession session, RequestMethod method) {
        return super.newParser(session, method).setNeedVerify(false);
    }

    /**
     * 调用示例-get：
     * curl -X POST -H "Content-Type: application/json" -d '{"User":{"id": 82001}}' http://localhost:9030/api/get
     */
    @PostMapping(value = "{method}")
    @Override
    public String crud(@PathVariable String method,
                       @RequestBody String request,
                       HttpSession session) {
        return super.crud(method, request, session);
    }

    @PostMapping("{method}/{tag}")
    @Override
    public String crudByTag(@PathVariable String method,
                            @PathVariable String tag,
                            @RequestParam Map<String, String> params,
                            @RequestBody String request,
                            HttpSession session) {
        return super.crudByTag(method, tag, params, request, session);
    }

    @GetMapping("get/{request}")
    public String openGet(@PathVariable String request, HttpSession session) {
        try {
            request = URLDecoder.decode(request, StringUtil.UTF_8);
        } catch (Exception e) {
        }
        return get(request, session);
    }

}
