package com.example.mycarsmt.dagger

import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.note.NoteServiceImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import com.example.mycarsmt.presentation.fragments.*
import com.example.mycarsmt.presentation.fragments.creators.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataBaseModule::class, ServicesModule::class])
interface AppComponent {

    fun injectModel(model: MainModel)
    fun injectModel(model: CarModel)
    fun injectModel(model: PartModel)
    fun injectModel(model: SettingModel)
    fun injectModel(model: CarCreatorModel)
    fun injectModel(model: PartCreatorModel)
    fun injectModel(model: NoteCreatorModel)
    fun injectModel(model: RepairCreatorModel)

    fun injectFragment(fragment: MainFragment)
    fun injectFragment(fragment: SettingFragment)
    fun injectFragment(fragment: PartFragment)
    fun injectFragment(fragment: CarFragment)
    fun injectFragment(fragment: CarCreator)
    fun injectFragment(fragment: PartCreator)
    fun injectFragment(fragment: NoteCreator)
    fun injectFragment(fragment: RepairCreator)

    fun injectService(service: CarServiceImpl)
    fun injectService(service: PartServiceImpl)
    fun injectService(service: NoteServiceImpl)
    fun injectService(service: RepairServiceImpl)

    @Component.Builder
    interface DataBuilder{
        fun createDataModule(module: DataBaseModule): DataBuilder
        fun createServiceModule(module: ServicesModule?): DataBuilder
        fun buildComponent(): AppComponent
    }
}