package easv.familiytracker.repository

import easv.familiytracker.models.BEFMember

class FMembersRepository {
    val fMembers = arrayOf<BEFMember>(
        BEFMember("Mor Julian", "123",  "1321", ""),
        BEFMember("Far Thiim", "1234", "1231", ""),
        BEFMember("Bror Christian", "12345",  "12341",""),
        BEFMember("Stepsis Herman", "6696969420", "123131",""),
    )

    fun getAll():Array<BEFMember> = fMembers
}