/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eulerframework.web.core.base.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import org.eulerframework.web.core.base.response.ErrorResponse;
import org.eulerframework.web.core.exception.web.SystemWebError;
import org.eulerframework.web.core.exception.web.WebException;

@ResponseBody
public abstract class ApiSupportWebController extends AbstractWebController {

//  /**
//   * 用于在程序发生{@link BindException}异常时统一返回错误信息
//   * 
//   * @return
//   */
//  @ResponseBody
//  @ResponseStatus(HttpStatus.BAD_REQUEST)
//  @ExceptionHandler(BindException.class)
//  public Object bindException(BindException e) {
//      this.logger.error(e.getMessage(), e);
//      return new ErrorResponse(new WebException(e.getMessage(), SystemWebError.ILLEGAL_PARAMETER, e));
//  }

    /**
     * 用于在程序发生{@link AccessDeniedException}异常时统一返回错误信息
     * 
     * @return 包含错误信息的Ajax响应体
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Object accessDeniedException(AccessDeniedException e) {
        return new ErrorResponse(new WebException(e.getMessage(), SystemWebError.ACCESS_DENIED, e));
    }
    
    /**
     * 用于在程序发生{@link MissingServletRequestPartException}异常时统一返回错误信息
     * 
     * @return 包含错误信息的Ajax响应体
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public Object missingServletRequestPartException(MissingServletRequestPartException e) {
        return new ErrorResponse(new WebException(e.getMessage(), SystemWebError.ILLEGAL_PARAMETER, e));
    }

    /**
     * 用于在程序发生{@link MissingServletRequestParameterException}异常时统一返回错误信息
     * 
     * @return 包含错误信息的Ajax响应体
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new ErrorResponse(new WebException(e.getMessage(), SystemWebError.ILLEGAL_PARAMETER, e));
    }

    /**
     * 用于在程序发生{@link IllegalArgumentException}异常时统一返回错误信息
     * 
     * @return 包含错误信息的Ajax响应体
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Object illegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse(new WebException(e.getMessage(), SystemWebError.ILLEGAL_ARGUMENT, e));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnrecognizedPropertyException.class)
    public Object unrecognizedPropertyException(UnrecognizedPropertyException e) {
        return new ErrorResponse(new WebException("JSON parse error: Unrecognized field \"" + e.getPropertyName() + "\"", SystemWebError.ILLEGAL_PARAMETER, e));
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object httpMessageNotReadableException(HttpMessageNotReadableException e) {
        if(e.getCause().getClass().equals(UnrecognizedPropertyException.class)) {
            return this.unrecognizedPropertyException((UnrecognizedPropertyException) e.getCause());
        }
        
        return new ErrorResponse(new WebException(e.getMessage(), SystemWebError.ILLEGAL_PARAMETER, e));
    }

    /**
     * 用于在程序发生{@link WebException}异常时统一返回错误信息
     * 
     * @return 包含错误信息的Ajax响应体
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebException.class)
    public Object webException(WebException e) {
        return new ErrorResponse(e);
    }

    /**
     * 用于在程序发生{@link Exception}异常时统一返回错误信息
     * 
     * @return 包含错误信息的Ajax响应体
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Object exception(Exception e) {
        this.logger.error(e.getMessage(), e);
        return new ErrorResponse();
    }
}
