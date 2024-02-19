package com.project.vetProject.core.config.modelMapper;

import org.modelmapper.ModelMapper;


public interface IModelMapperService {
    ModelMapper forRequest();
    ModelMapper forResponse();
}
