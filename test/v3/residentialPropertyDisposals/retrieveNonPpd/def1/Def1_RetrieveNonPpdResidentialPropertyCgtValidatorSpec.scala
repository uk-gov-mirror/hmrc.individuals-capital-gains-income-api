/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v3.residentialPropertyDisposals.retrieveNonPpd.def1

import shared.models.domain.{Nino, TaxYear}
import shared.models.errors.*
import support.UnitSpec
import v3.residentialPropertyDisposals.retrieveNonPpd.RetrieveNonPpdResidentialPropertyCgtValidatorFactory
import v3.residentialPropertyDisposals.retrieveNonPpd.def1.model.request.Def1_RetrieveNonPpdResidentialPropertyRequestData
import v3.residentialPropertyDisposals.retrieveNonPpd.model.request.RetrieveNonPpdResidentialPropertyCgtRequestData

class Def1_RetrieveNonPpdResidentialPropertyCgtValidatorSpec extends UnitSpec {
  private implicit val correlationId: String = "1234"
  private val validNino                      = "AA123456A"
  private val validTaxYear                   = "2024-25"

  private val parsedNino    = Nino(validNino)
  private val parsedTaxYear = TaxYear.fromMtd(validTaxYear)

  private val validatorFactory                         = new RetrieveNonPpdResidentialPropertyCgtValidatorFactory
  private def validator(nino: String, taxYear: String) = validatorFactory.validator(nino, taxYear)

  "running a validation" should {
    "return no errors" when {
      "a valid request is supplied" in {
        val result: Either[ErrorWrapper, RetrieveNonPpdResidentialPropertyCgtRequestData] =
          validator(validNino, validTaxYear).validateAndWrapResult()

        result shouldBe Right(Def1_RetrieveNonPpdResidentialPropertyRequestData(parsedNino, parsedTaxYear))
      }
    }

    "return NinoFormatError error" when {
      "an invalid nino is supplied" in {
        val result: Either[ErrorWrapper, RetrieveNonPpdResidentialPropertyCgtRequestData] =
          validator("A12344A", validTaxYear).validateAndWrapResult()

        result shouldBe Left(ErrorWrapper(correlationId, NinoFormatError))
      }
    }
  }

}
