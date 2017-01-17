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

package uk.gov.hmrc.bforms.models.persistence

import java.time.LocalDate

import org.apache.commons.lang3.RandomStringUtils
import play.api.libs.json.{Format, JsError, JsResult, JsString, JsSuccess, JsValue, Json, _}
import uk.gov.hmrc.bforms.models.EnvironmentalBody

case class LandfillTaxDetailsDeclarationPersistence(registrationNumber : GovernmentGatewayId = GovernmentGatewayId(RandomStringUtils.random(4)),
                                         nameOfBusiness: NameOfBusiness = new NameOfBusiness(""),
                                         accountingPeriodStartDate: LocalDate = LocalDate.now,
                                         accountingPeriodEndDate: LocalDate = LocalDate.now,
                                         taxDueForThisPeriod: TaxDueForThisPeriod = new TaxDueForThisPeriod(""),
                                         underDeclarationsFromPreviousPeriod: UnderDeclarationsFromPreviousPeriod = new UnderDeclarationsFromPreviousPeriod(""),
                                         overDeclarationsForThisPeriod: OverDeclarationsForThisPeriod = new OverDeclarationsForThisPeriod(""),
                                         taxCreditClaimedForEnvironment: TaxCreditClaimedForEnvironment = new TaxCreditClaimedForEnvironment(0),
                                         badDebtReliefClaimed: BadDebtReliefClaimed = new BadDebtReliefClaimed(""),
                                         otherCredits: OtherCredits = new OtherCredits(""),
                                         standardRateWaste: StandardRateWaste = new StandardRateWaste(""),
                                         lowerRateWaste: LowerRateWaste = new LowerRateWaste(""),
                                         exemptWaste: ExemptWaste = new ExemptWaste(""),
                                         environmentalBody1: Seq[EnvironmentalBody] =  Seq(EnvironmentalBody("default" , 0)),
                                         emailAddress: EmailAddress = new EmailAddress(Some("")),
                                         confirmEmailAddress: ConfirmEmailAddress = new ConfirmEmailAddress(Some("")),
                                         datePersisted : LocalDate = LocalDate.now
                                        ){
}


case class EitherLandfillTaxDetailsDeclarationPersistenceMapStringString(value : Either[LandfillTaxDetailsDeclarationPersistence, Map[String, String]])

object LandfillTaxDetailsDeclarationPersistence {

  def apply(governmentGateway : GovernmentGatewayId,
            nameOfBusiness: NameOfBusiness,
            accountingPeriodStartDate: LocalDate,
            accountingPeriodEndDate: LocalDate,
            taxDueForThisPeriod: TaxDueForThisPeriod,
            underDeclarationsFromPreviousPeriod: UnderDeclarationsFromPreviousPeriod,
            overDeclarationsForThisPeriod: OverDeclarationsForThisPeriod,
            taxCreditClaimedForEnvironment: TaxCreditClaimedForEnvironment,
            badDebtReliefClaimed: BadDebtReliefClaimed,
            otherCredits: OtherCredits,
            standardRateWaste: StandardRateWaste,
            lowerRateWaste: LowerRateWaste,
            exemptWaste: ExemptWaste,
            environmentalBody: Seq[EnvironmentalBody],
            emailAddress: EmailAddress,
            confirmEmailAddress: ConfirmEmailAddress) = {

    new LandfillTaxDetailsDeclarationPersistence(governmentGateway,
      nameOfBusiness,
      accountingPeriodStartDate,
      accountingPeriodEndDate,
      taxDueForThisPeriod,
      underDeclarationsFromPreviousPeriod,
      overDeclarationsForThisPeriod,
      taxCreditClaimedForEnvironment,
      badDebtReliefClaimed,
      otherCredits,
      standardRateWaste,
      lowerRateWaste,
      exemptWaste,
      environmentalBody,
      emailAddress,
      confirmEmailAddress)
  }

  val mongoFormat = Json.format[LandfillTaxDetailsDeclarationPersistence]
}

object EitherLandfillTaxDetailsDeclarationPersistenceMapStringString {

  implicit val formatLandfill : Format[LandfillTaxDetailsDeclarationPersistence] = LandfillTaxDetailsDeclarationPersistence.mongoFormat

  def apply(value: LandfillTaxDetailsDeclarationPersistence) = new EitherLandfillTaxDetailsDeclarationPersistenceMapStringString(Left(value))

  def apply(value: Map[String, String]) = new EitherLandfillTaxDetailsDeclarationPersistenceMapStringString(Right(value))

  implicit val format : Format[Either[LandfillTaxDetailsDeclarationPersistence, Map[String, String]]] = ValueClassFormatEitherLandfillTaxDetailsDeclarationPersistenceMapStringString.format[LandfillTaxDetailsDeclarationPersistence, Map[String, String]]
}

object ValueClassFormatEitherLandfillTaxDetailsDeclarationPersistenceMapStringString {

  def format[A <:LandfillTaxDetailsDeclarationPersistence, B <:Map[String, String]](implicit readL: Reads[A], readR: Reads[B], writeL: Writes[A], writeR: Writes[B]) : Format[Either[A, B]] = {
    new Format[Either[A, B]] {
      def reads(json: JsValue) : JsResult[Either[A, B]] = {
        json match {
          case o @ JsObject(_) =>
            (o.value.get("object"), o.value.get("map")) match {
              case (Some(obj), None) => {
                println("object")
                readL.reads(obj).map(Left(_))
              }
              case (None, Some(obj)) => {
                println("map")
                readR.reads(obj).map(Right(_))
              }
              case (unknownL, unknownR) => JsError(
                s"""|Expected 'object' or 'map' for Either[L, R] type.
                    |UnknownL: $unknownL
                    |UnkownR : $unknownR
                   """.stripMargin)
            }
          case unknown => JsError(s"JsObject expected got $unknown")
        }
      }

      def writes(v: Either[A, B]) : JsValue = {
        v match {
          case Left(left) => {
            println("Left(left)")
            Json.obj("object" -> writeL.writes(left))
          }
          case Right(right) => {
            println("Right(right)")
            Json.obj("map" -> writeR.writes(right))
          }
        }
      }
    }
  }
}

