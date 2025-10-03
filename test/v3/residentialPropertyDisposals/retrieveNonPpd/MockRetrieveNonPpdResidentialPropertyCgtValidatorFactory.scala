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

package v3.residentialPropertyDisposals.retrieveNonPpd

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory
import org.scalatest.TestSuite
import shared.controllers.validators.Validator
import shared.models.errors.MtdError
import v3.residentialPropertyDisposals.retrieveNonPpd.model.request.RetrieveNonPpdResidentialPropertyCgtRequestData

trait MockRetrieveNonPpdResidentialPropertyCgtValidatorFactory extends TestSuite with MockFactory {

  val mockRetrieveNonPpdResidentialPropertyCgtValidatorFactory: RetrieveNonPpdResidentialPropertyCgtValidatorFactory =
    mock[RetrieveNonPpdResidentialPropertyCgtValidatorFactory]

  object MockedRetrieveNonPpdResidentialPropertyCgtValidatorFactory {

    def validator(): CallHandler[Validator[RetrieveNonPpdResidentialPropertyCgtRequestData]] =
      (mockRetrieveNonPpdResidentialPropertyCgtValidatorFactory.validator(_: String, _: String)).expects(*, *)

  }

  def willUseValidator(
      use: Validator[RetrieveNonPpdResidentialPropertyCgtRequestData]): CallHandler[Validator[RetrieveNonPpdResidentialPropertyCgtRequestData]] = {
    MockedRetrieveNonPpdResidentialPropertyCgtValidatorFactory
      .validator()
      .anyNumberOfTimes()
      .returns(use)
  }

  def returningSuccess(result: RetrieveNonPpdResidentialPropertyCgtRequestData): Validator[RetrieveNonPpdResidentialPropertyCgtRequestData] =
    new Validator[RetrieveNonPpdResidentialPropertyCgtRequestData] {
      def validate: Validated[Seq[MtdError], RetrieveNonPpdResidentialPropertyCgtRequestData] = Valid(result)
    }

  def returning(result: MtdError*): Validator[RetrieveNonPpdResidentialPropertyCgtRequestData] = returningErrors(result)

  def returningErrors(result: Seq[MtdError]): Validator[RetrieveNonPpdResidentialPropertyCgtRequestData] =
    new Validator[RetrieveNonPpdResidentialPropertyCgtRequestData] {
      def validate: Validated[Seq[MtdError], RetrieveNonPpdResidentialPropertyCgtRequestData] = Invalid(result)
    }

}
