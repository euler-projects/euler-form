package net.eulerform.web.core.base.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.http.HttpStatus;

@XmlRootElement
public class WebServiceResponse<T> {

    public WebServiceResponse() {
        this.setStatusToNoContent();
    }

    public WebServiceResponse(T data) {
        if (data == null) {
            this.setStatusToNoContent();
            return;
        }
        this.setData(data);
        this.setStatus(HttpStatus.OK);
    }

    public WebServiceResponse(List<T> data) {
        if (data == null || data.isEmpty()) {
            this.setStatusToNoContent();
            return;
        }
        this.setData(data);
        this.setStatus(HttpStatus.OK);
    }

    public WebServiceResponse(T data, WebServiceResponseStatus webServiceResponseStatus) {
        this.setData(data);
        this.setStatus(webServiceResponseStatus);
    }

    public WebServiceResponse(List<T> data, WebServiceResponseStatus webServiceResponseStatus) {
        this.setData(data);
        this.setStatus(webServiceResponseStatus);
    }

    public WebServiceResponse(T data, HttpStatus httpStatus) {
        this.setData(data);
        this.setStatus(httpStatus);
    }

    public WebServiceResponse(List<T> data, HttpStatus httpStatus) {
        this.setData(data);
        this.setStatus(httpStatus);
    }

    public WebServiceResponse(HttpStatus httpStatus) {
        this.data = null;
        this.dataSize = 0;
        this.setStatus(httpStatus);
    }

    public WebServiceResponse(WebServiceResponseStatus webServiceResponseStatus) {
        this.data = null;
        this.dataSize = 0;
        this.setStatus(webServiceResponseStatus);
    }

    private List<T> data;

    private Integer dataSize;

    private Integer statusCode;

    private String statusInfo;

    private Date returnDate;

    public void setData(T data) {
        if (data == null) {
            this.setStatusToNoContent();
            return;
        }

        List<T> dataList = new ArrayList<>();
        dataList.add(data);
        this.setData(dataList);
    }

    public void setData(List<T> data) {
        if (data == null || data.isEmpty()) {
            this.setStatusToNoContent();
            return;
        }

        this.data = data;
        this.dataSize = data.size();
    }

    @XmlElement
    public List<T> getData() {
        return data;
    }

    @XmlElement
    public Integer getStatusCode() {
        return statusCode;
    }

    @XmlElement
    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatus(HttpStatus httpStatus) {
        this.statusCode = httpStatus.value();
        this.statusInfo = httpStatus.getReasonPhrase();
    }

    public void setStatus(WebServiceResponseStatus webServiceResponseStatus) {
        this.statusCode = webServiceResponseStatus.getStatusCode();
        this.statusInfo = webServiceResponseStatus.getStatusInfo();
    }

    public void setStatus(int statusCode, String statusInfo) {
        this.statusCode = statusCode;
        this.statusInfo = statusInfo;
    }

    @XmlElement
    public Integer getDataSize() {
        return this.dataSize;
    }

    @XmlElement
    public Date getReturnDate() {
        return returnDate == null ? new Date() : this.returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    private void setStatusToNoContent() {
        this.data = null;
        this.dataSize = 0;
        this.setStatus(HttpStatus.NO_CONTENT);
    }
}
