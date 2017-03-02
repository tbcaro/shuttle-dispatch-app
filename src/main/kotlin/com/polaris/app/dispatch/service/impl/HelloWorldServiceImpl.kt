package com.polaris.app.dispatch.service.impl

import com.polaris.app.dispatch.repository.HelloWorldRepository
import com.polaris.app.dispatch.service.HelloWorldService
import com.polaris.app.dispatch.service.bo.HelloWorld


class HelloWorldServiceImpl(val helloWorldRepository: HelloWorldRepository) : HelloWorldService {
    override fun retrieveAll(): List<HelloWorld> {
        val helloWorlds = arrayListOf<HelloWorld>()
        val helloWorldEntities = this.helloWorldRepository.findAll()

        helloWorldEntities.forEach {
            val helloWorld = HelloWorld(
                    helloWorld = it.helloWorld
            )
            helloWorlds.add(helloWorld)
        }

        return helloWorlds
    }
}