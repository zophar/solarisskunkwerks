/*
Copyright (c) 2008~2009, Justin R. Bengtson (poopshotgun@yahoo.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
        this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
    * Neither the name of Justin R. Bengtson nor the names of contributors may
        be used to endorse or promote products derived from this software
        without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package ssw.components;

import java.util.Vector;

public class TargetingComputer extends abPlaceable {
    private ifLoadout Owner;
    private final static AvailableCode ISAC = new AvailableCode( false, 'E', 'X', 'X', 'E', 3062, 0, 0, "FC", "", false, false );
    private final static AvailableCode CLAC = new AvailableCode( true, 'E', 'X', 'D', 'C', 2860, 0, 0, "CMN", "", false, false );

    public TargetingComputer( ifLoadout l ) {
        Owner = l;
        SetExclusions( new Exclusion( new String[] { "A.E.S." }, "Targeting Computer" ) );
    }

    @Override
    public String GetCritName() {
        return "Targeting Computer";
    }

    public String GetMMName( boolean UseRear ) {
        if( Owner.GetMech().IsClan() ) {
            return "CLTargeting Computer";
        } else {
            return "ISTargeting Computer";
        }
    }

    @Override
    public int NumCrits() {
        return GetSize();
    }

    @Override
    public float GetTonnage() {
        if( IsArmored() ) {
            return GetSize() + GetSize() * 0.5f;
        } else {
            return GetSize();
        }
    }

    @Override
    public float GetCost() {
        if( IsArmored() ) {
            return GetSize() * 10000.0f + GetSize() * 150000.0f;
        } else {
            return GetSize() * 10000.0f;
        }
    }

    public float GetOffensiveBV() {
        // we can't really control this one, so we should always call the TC
        // with UseCurOffensiveBV().  Since almost all of the time a 'Mech will
        // mount it's weapons forward, we'll call it without using rear.
        return GetCurOffensiveBV( false, false, false );
    }

    public float GetCurOffensiveBV( boolean UseRear, boolean UseTC, boolean UseAES ) {
        // BV calculations for the targeting computer are based on modified
        // weapon BV, which we won't know until later.
        return 0.0f;
    }

    public float GetDefensiveBV() {
        float retval = 0.0f;
        if( IsArmored() ) {
            retval = 5.0f * NumCrits();
        }
        return retval;
    }

    @Override
    public AvailableCode GetAvailability() {
        AvailableCode retval;
        if( Owner.GetMech().IsClan() ) {
            retval = new AvailableCode( CLAC.IsClan(), CLAC.GetTechRating(), CLAC.GetSLCode(), CLAC.GetSWCode(), CLAC.GetCICode(), CLAC.GetIntroDate(), CLAC.GetExtinctDate(), CLAC.GetReIntroDate(), CLAC.GetIntroFaction(), CLAC.GetReIntroFaction(), CLAC.WentExtinct(), CLAC.WasReIntroduced(), CLAC.GetRandDStart(), CLAC.IsPrototype(), CLAC.GetRandDFaction(), CLAC.GetRulesLevelBM(), CLAC.GetRulesLevelIM() );
        } else {
            retval = new AvailableCode( ISAC.IsClan(), ISAC.GetTechRating(), ISAC.GetSLCode(), ISAC.GetSWCode(), ISAC.GetCICode(), ISAC.GetIntroDate(), ISAC.GetExtinctDate(), ISAC.GetReIntroDate(), ISAC.GetIntroFaction(), ISAC.GetReIntroFaction(), ISAC.WentExtinct(), ISAC.WasReIntroduced(), ISAC.GetRandDStart(), ISAC.IsPrototype(), ISAC.GetRandDFaction(), ISAC.GetRulesLevelBM(), ISAC.GetRulesLevelIM()  );
        }
        if( IsArmored() ) {
            if( retval.IsClan() ) {
                retval.Combine( CLArmoredAC );
            } else {
                retval.Combine( ISArmoredAC );
            }
        }
        return retval;
    }

    private int GetSize() {
        float Build = 0.0f;
        Vector V = Owner.GetTCList();

        if( V.size() == 0 ) {
            return 0;
        }

        for( int i = 0; i < V.size(); i++ ) {
            if( V.get( i ) instanceof EnergyWeapon ) {
                if( ((EnergyWeapon) V.get( i )).HasCapacitor() ) {
                    Build += ((abPlaceable) V.get( i )).GetTonnage() - 1.0f;
                } else {
                    Build += ((abPlaceable) V.get( i )).GetTonnage();
                }
            } else {
                Build += ((abPlaceable) V.get( i )).GetTonnage();
            }
        }

        if( Owner.GetMech().IsClan() ) {
            return (int) Math.floor( Build * 0.2f + 0.999f );
        } else {
            return (int) Math.floor( Build * 0.25f + 0.999f );
        }
    }

    @Override
    public boolean CoreComponent() {
        return true;
    }

    @Override
    public String toString() {
        return GetCritName();
    }
}
