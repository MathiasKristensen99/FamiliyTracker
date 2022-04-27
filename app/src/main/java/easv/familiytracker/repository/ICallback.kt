package easv.familiytracker.repository

import easv.familiytracker.models.BEFMember

interface ICallback {
    fun familyMembers(members: List<BEFMember>)


}