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

import julienrf.json.derived
import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.bforms.core.Expr

sealed trait ComponentType

case object Text extends ComponentType

case object Date extends ComponentType

case object Address extends ComponentType

object ComponentType {
  implicit val format: OFormat[ComponentType] = derived.oformat
}

case class FieldValue(
                       id: FieldId,
                       `type`: Option[ComponentType],
                       label: String,
                       value: Option[Expr],
                       format: Option[String],
                       helpText: Option[String],
                       readOnly: Option[String],
                       mandatory: Option[String])

object FieldValue {
  implicit val format = Json.format[FieldValue]
}
