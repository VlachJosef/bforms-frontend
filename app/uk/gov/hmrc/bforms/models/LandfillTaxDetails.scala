/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.bforms.models

import java.time.LocalDate

import play.api.data.{Form, Mapping}
import play.api.data.validation._
import play.api.data.Forms._
import play.api.libs.json.Json


case class LandfillTaxDetails(
                              save:String,
                               firstName: String,
                               lastName: String,
                               telephoneNumber: String,
                               status: String,
                               nameOfBusiness: String,
                               accountingPeriodStartDate: LocalDate,
                               accountingPeriodEndDate: LocalDate,
                               taxDueForThisPeriod: String,
                               underDeclarationsFromPreviousPeriod: String,
                               overDeclarationsForThisPeriod: String,
                               taxCreditClaimedForEnvironment: BigDecimal,
                               badDebtReliefClaimed: String,
                               otherCredits: String,
                               standardRateWaste: String,
                               lowerRateWaste: String,
                               exemptWaste: String,
                               environmentalBody1: BigDecimal,
                               environmentalBody2: Option[BigDecimal],
                               emailAddress: Option[String],
                               confirmEmailAddress: Option[String]
                             )

object LandfillTaxDetails {
  implicit val formats = Json.format[LandfillTaxDetails]

  val landfillTaxDetailsMapping : Mapping[LandfillTaxDetails]
  = {
    mapping(
      "save" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "telephoneNumber" -> nonEmptyText(minLength = 3),
      "status" -> nonEmptyText,
      "nameOfBusiness" -> nonEmptyText,
      "accountingPeriodStartDate" -> localDate("dd/MM/yyyy"),
      "accountingPeriodEndDate" -> localDate("dd/MM/yyyy"),
      "taxDueForThisPeriod" -> nonEmptyText,
      "underDeclarationsFromPreviousPeriod" -> nonEmptyText,
      "overDeclarationsForThisPeriod" -> nonEmptyText,
      "taxCreditClaimedForEnvironment" -> bigDecimal,
      "badDebtReliefClaimed" -> nonEmptyText,
      "otherCredits" -> nonEmptyText,
      "standardRateWaste" -> nonEmptyText,
      "lowerRateWaste" -> nonEmptyText,
      "exemptWaste" -> nonEmptyText,
      "environmentalBody1" -> bigDecimal,
      "environmentalBody2" -> optional(bigDecimal),
      "emailAddress" -> optional(text),
      "confirmEmailAddress" -> optional(text)
    )(LandfillTaxDetails.apply)(LandfillTaxDetails.unapply)
  }

  def validateEmail(fields : LandfillTaxDetails) : Boolean = {
    fields match {
      case landfillTaxDetails => landfillTaxDetails.emailAddress match {
        case Some(e) if (e != landfillTaxDetails.confirmEmailAddress.getOrElse("")) => false
        case _ => true
      }
    }
  }

  val form = Form(landfillTaxDetailsMapping
      .verifying("Email address does not match", validateEmail _)

  )


}
