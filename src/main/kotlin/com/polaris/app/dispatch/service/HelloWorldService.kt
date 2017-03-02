package com.polaris.app.dispatch.service

import com.polaris.app.dispatch.service.bo.HelloWorld


interface HelloWorldService {
    fun retrieveAll(): List<HelloWorld>
}