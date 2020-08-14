package com.example.mycarsmt.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarsmt.Directories
import com.example.mycarsmt.R
import com.example.mycarsmt.SpecialWords.Companion.CAR
import com.example.mycarsmt.SpecialWords.Companion.NOTE
import com.example.mycarsmt.SpecialWords.Companion.NO_PHOTO
import com.example.mycarsmt.SpecialWords.Companion.PART
import com.example.mycarsmt.SpecialWords.Companion.REPAIR
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.enums.ContentType
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import com.example.mycarsmt.presentation.adapters.NoteItemAdapter
import com.example.mycarsmt.presentation.adapters.RepairItemAdapter
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_part.*
import java.io.File
import javax.inject.Inject

class PartFragment @Inject constructor() : Fragment(R.layout.fragment_part) {

    companion object {
        const val FRAG_TAG = "partFragment"
        const val TAG = "testmt"
    }

    @Inject
    lateinit var model: PartModel

    private lateinit var part: Part
    private lateinit var car: Car
    private var notes: List<Note> = emptyList()
    private var repairs: List<Repair> = emptyList()
    private var contentType = ContentType.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.component.injectFragment(this)
        arguments?.containsKey(CAR)?.let { car = arguments?.getSerializable(CAR) as Car }
        part = arguments?.getSerializable(PART) as Part
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContentType(ContentType.DEFAULT)
        loadModel()
        setRecycler()
        setButtons()
    }

    @SuppressLint("CheckResult")
    private fun loadModel() {
        model.carService.readById(car.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { value ->
                car = value
            }

        model.partService.readById(part.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {value ->
                part = value
                loadPartData()
            }

        model.noteService.readAllForPart(part)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{value ->
                notes = value
            }

        model.repairService.readAllForPart(part)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{value ->
                repairs = value
            }
    }

    private fun setButtons(){
        partPanelButtonNotes.setOnClickListener {
            setContentType(ContentType.NOTES)
        }
        partPanelButtonButtonRepairs.setOnClickListener {
            setContentType(ContentType.REPAIRS)
        }
        partPanelButtonService.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(PART, part)
            view?.findNavController()?.navigate(R.id.action_partFragment_to_serviceFragmentDialog, bundle)
            // snack about service
        }
        partPanelFABSet.setOnClickListener {
            setContentType(ContentType.DEFAULT)
            val bundle = Bundle()
            bundle.putSerializable(PART, part)
            view?.findNavController()?.navigate(R.id.action_partFragment_to_partCreator, bundle)
        }
        partPanelButtonCancel.setOnClickListener {
            if (contentType == ContentType.DEFAULT) {
                val bundle = Bundle()
                bundle.putSerializable(CAR, car)
                view?.findNavController()?.navigate(R.id.action_partFragment_to_carFragment, bundle)
            } else {
                hideRecycler()
            }
        }
        partPanelRecyclerAddButton.setOnClickListener {
            when (contentType) {
                ContentType.NOTES -> {
                    val note = Note()
                    note.carId = part.carId
                    note.partId = part.id
                    note.mileage = part.mileage

                    val bundle = Bundle()
                    bundle.putSerializable(NOTE, note)
                    bundle.putSerializable(PART, part)
                    view?.findNavController()?.navigate(R.id.action_partFragment_to_repairCreator, bundle)

                }
                ContentType.REPAIRS -> {
                    val repair = Repair()
                    repair.carId = part.carId
                    repair.partId = part.id
                    repair.mileage = part.mileage

                    val bundle = Bundle()
                    bundle.putSerializable(REPAIR, repair)
                    bundle.putSerializable(PART, part)
                    view?.findNavController()?.navigate(R.id.action_partFragment_to_repairCreator, bundle)
                }
                else -> {

                }
            }
        }
    }

    private fun setContentType(type: ContentType) {
        showProgressBar()
        contentType = type
        setAdapter()
    }

    @SuppressLint("SetTextI18n")
    private fun loadPartData() {
        setTitle("${part.name} (${car.model} ${car.number})")

        partCreatorTextName.text = part.name
        partPanelTextToChangeDKM.text = part.getInfoToChange()
        partPanelTextInstallAtKM.text = "${part.mileageLastChange} km"
        partPanelTextInstallAtDate.text = "${part.dateLastChange}"
        partPanelTextListOfCodes.text = part.codes
        partPanelTextDescriptionBody.text = part.description

        loadPhoto()
    }

    private fun setRecycler() {
        partPanelRecycler.layoutManager =
            LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setAdapter() {
        hideProgressBar()
        when (contentType) {
            ContentType.DEFAULT -> {
                hideRecycler()
            }
            ContentType.NOTES -> {
                showRecycler()
                checkOnEmpty(notes)
                partPanelRecycler.adapter = NoteItemAdapter(notes, object :
                    NoteItemAdapter.OnItemClickListener {
                    override fun onClick(note: Note) {
                        val bundle = Bundle()
                        bundle.putSerializable(NOTE, note)
                        bundle.putSerializable(PART, part)
                        view?.findNavController()?.navigate(R.id.action_partFragment_to_noteCreator, bundle)
                    }
                })
            }
            ContentType.REPAIRS -> {
                showRecycler()
                checkOnEmpty(repairs)
                partPanelRecycler.adapter = RepairItemAdapter(repairs, object :
                    RepairItemAdapter.OnItemClickListener {
                    override fun onClick(repair: Repair) {
                        val bundle = Bundle()
                        bundle.putSerializable(REPAIR, repair)
                        bundle.putSerializable(PART, part)
                        view?.findNavController()?.navigate(R.id.action_partFragment_to_repairCreator, bundle)
                    }
                })
            }
            else -> {

            }
        }
    }

    private fun loadPhoto() {
        if (part.photo == NO_PHOTO || part.photo.isEmpty()) {
            Picasso.get().load(R.drawable.nophoto).into(partPanelMainImage)
        } else {
            Picasso.get().load(File(Directories.PART_IMAGE_DIRECTORY.value, "${part.photo}.jpg"))
                .into(partPanelMainImage)
        }
    }

    private fun checkOnEmpty(list: List<*>) {
        if (list.isEmpty()) partPanelNoElements.visibility = View.VISIBLE
        else partPanelNoElements.visibility = View.GONE
    }

    private fun hideRecycler() {
        partPanelRecycler.visibility = View.GONE
        partPanelRecyclerAddButton.visibility = View.GONE

        partPanelTextInstalledInfo.visibility = View.VISIBLE
        partPanelTextInstallAtDate.visibility = View.VISIBLE
        partPanelTextInstallAtKM.visibility = View.VISIBLE
        partPanelTextDescription.visibility = View.VISIBLE
        partPanelTextDescriptionBody.visibility = View.VISIBLE
        partPanelTextListOfCodes.visibility = View.VISIBLE
        partPanelTextCodesAndCross.visibility = View.VISIBLE

        partPanelNoElements.visibility = View.GONE
        contentType = ContentType.DEFAULT
    }

    private fun showRecycler() {
        partPanelTextInstalledInfo.visibility = View.GONE
        partPanelTextInstallAtDate.visibility = View.GONE
        partPanelTextInstallAtKM.visibility = View.GONE
        partPanelTextDescription.visibility = View.GONE
        partPanelTextDescriptionBody.visibility = View.GONE
        partPanelTextListOfCodes.visibility = View.GONE
        partPanelTextCodesAndCross.visibility = View.GONE

        partPanelRecycler.visibility = View.VISIBLE
        partPanelRecyclerAddButton.visibility = View.VISIBLE
    }

    private fun setTitle(title: String){
        activity?.title = title
    }

    private fun showProgressBar(){
        partProgress.visibility = View.VISIBLE
        partProgressText.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        partProgress.visibility = View.INVISIBLE
        partProgressText.visibility = View.INVISIBLE
    }
}