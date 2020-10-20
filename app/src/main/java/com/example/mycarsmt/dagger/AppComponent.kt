package com.example.mycarsmt.dagger

import com.example.mycarsmt.backServices.TXTConverter
import com.example.mycarsmt.datalayer.di.DataBaseModule
import com.example.mycarsmt.domain.service.car.CarRepositoryImpl
import com.example.mycarsmt.domain.service.note.NoteServiceImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import com.example.mycarsmt.presentation.fragments.*
import com.example.mycarsmt.presentation.fragments.creators.*
import com.example.mycarsmt.presentation.fragments.dialogs.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataBaseModule::class, RepositoryModule::class])
interface AppComponent {

    fun injectTXTConverter(model: TXTConverter)

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

    fun injectDialog(dialog: CarDeleteDialog)
    fun injectDialog(dialog: PartDeleteDialog)
    fun injectDialog(dialog: RepairDeleteDialog)
    fun injectDialog(dialog: ServiceFragmentDialog)
    fun injectDialog(dialog: MileageFragmentDialog)

    fun injectService(service: CarRepositoryImpl)
    fun injectService(service: PartServiceImpl)
    fun injectService(service: NoteServiceImpl)
    fun injectService(service: RepairServiceImpl)

    @Component.Builder
    interface DataBuilder{
        fun createDataModule(module: DataBaseModule): DataBuilder
        fun createRepositoryModule(module: RepositoryModule): DataBuilder
        fun buildComponent(): AppComponent
    }
}