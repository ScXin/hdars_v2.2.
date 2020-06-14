package com.hlsii.controller;

import com.hlsii.service.IArchiverSystemService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ScXin
 * @date 4/26/2020 12:37 PM
 */
public class MGMTBaseController {

    @Autowired
    protected IArchiverSystemService archiverSystemService;
}
